package com.imaec.wishplace.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.imaec.wishplace.*
import com.imaec.wishplace.base.BaseActivity
import com.imaec.wishplace.databinding.ActivityListBinding
import com.imaec.wishplace.repository.PlaceRepository
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.PlaceDao
import com.imaec.wishplace.room.entity.PlaceEntity
import com.imaec.wishplace.ui.util.PlaceItemDecoration
import com.imaec.wishplace.ui.view.dialog.CommonDialog
import com.imaec.wishplace.ui.view.dialog.EditDialog
import com.imaec.wishplace.utils.SharedPreferenceManager
import com.imaec.wishplace.viewmodel.ListViewModel

var currentNativeAd: UnifiedNativeAd? = null

class ListActivity : BaseActivity<ActivityListBinding>(R.layout.activity_list) {

    private lateinit var viewModel: ListViewModel
    private lateinit var placeDao: PlaceDao
    private lateinit var placeRepository: PlaceRepository
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var itemDecoration: PlaceItemDecoration

    private var isUpdated = false
    private var isRemoveAd = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()

        viewModel = getViewModel(ListViewModel::class.java, placeRepository)

        binding.apply {
            lifecycleOwner = this@ListActivity
            viewModel = this@ListActivity.viewModel
            recyclerList.layoutManager = gridLayoutManager
            recyclerList.addItemDecoration(itemDecoration)
        }

        viewModel.apply {
            setCategory(intent.getStringExtra(EXTRA_CATEGORY))
            addOnClickListener { entity, view ->
                if (entity is PlaceEntity) {
                    startActivityForResult(Intent(this@ListActivity, DetailActivity::class.java).apply {
                        putExtra(EXTRA_PLACE_ID, entity.placeId)
                        putExtra(EXTRA_CATEGORY, entity.category)
                        putExtra(EXTRA_TITLE, entity.name)
                        putExtra(EXTRA_ADDRESS, entity.address)
                        putExtra(EXTRA_IMG_URL, entity.imageUrl)
                        putExtra(EXTRA_SITE_URL, entity.siteUrl)
                        putExtra(EXTRA_IS_VISIT, entity.visitFlag)
                    }, 0, getTransitionOption(view).toBundle())
                }
            }
            addOnLongClickListener { entity ->
                val dialog = EditDialog(this@ListActivity).apply {
                    setTitle(entity.name)
                    setOnEditClickListener(View.OnClickListener {
                        startActivityForResult(Intent(context, EditActivity::class.java).apply {
                            putExtra(EXTRA_PLACE_ID, entity.placeId)
                            putExtra(EXTRA_CATEGORY, entity.category)
                            putExtra(EXTRA_TITLE, entity.name)
                            putExtra(EXTRA_ADDRESS, entity.address)
                            putExtra(EXTRA_IMG_URL, entity.imageUrl)
                            putExtra(EXTRA_SITE_URL, entity.siteUrl)
                            putExtra(EXTRA_CONTENT, entity.content)
                            putExtra(EXTRA_IS_VISIT, entity.visitFlag)
                        }, 0)
                        dismiss()
                    })
                    setOnDeleteClickListener(View.OnClickListener {
                        delete(entity)
                        dismiss()
                    })
                }
                dialog.show()
            }
        }

        if (isRemoveAd) {
            currentNativeAd = null
            viewModel.getData(intent.getIntExtra(EXTRA_CATEGORY_ID, 0), null)
        } else {
            initNativeAd()
        }
    }

    override fun onDestroy() {
        currentNativeAd?.destroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            RESULT_EDIT, RESULT_DELETE -> {
                viewModel.getData(intent.getIntExtra(EXTRA_CATEGORY_ID, 0), currentNativeAd)
            }
        }
        data?.let {
            isUpdated = it.getBooleanExtra(EXTRA_IS_UPDATED, false)
        }
    }

    override fun onBackPressed() {
        if (isUpdated) setResult(RESULT_EDIT)
        super.onBackPressed()
    }

    private fun init() {
        isRemoveAd = SharedPreferenceManager.getBool(this, SharedPreferenceManager.KEY.PREF_REMOVE_AD, false)

        placeDao = AppDatabase.getInstance(this).placeDao()
        placeRepository = PlaceRepository.getInstance(placeDao)
        gridLayoutManager = GridLayoutManager(this, 2)
        itemDecoration = PlaceItemDecoration(this)
    }

    private fun initNativeAd() {
        showProgress()
        val builder = AdLoader.Builder(this, getString(R.string.ad_id_list_native))
        builder.forUnifiedNativeAd { unifiedNativeAd ->
            if (isDestroyed) {
                unifiedNativeAd.destroy()
                return@forUnifiedNativeAd
            }
            currentNativeAd?.destroy()
            currentNativeAd = unifiedNativeAd

            viewModel.getData(intent.getIntExtra(EXTRA_CATEGORY_ID, 0), unifiedNativeAd)
            hideProgress()
        }

        val videoOptions = VideoOptions.Builder()
            .setStartMuted(true)
            .build()
        val adOptions = NativeAdOptions.Builder()
            .setVideoOptions(videoOptions)
            .build()
        builder.withNativeAdOptions(adOptions)

        val adLoader = builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(errorCode: Int) {
                viewModel.getData(intent.getIntExtra(EXTRA_CATEGORY_ID, 0), null)
                hideProgress()
            }
        }).build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun delete(entity: PlaceEntity) {
        CommonDialog(this, "'${entity.name}' ${getString(R.string.msg_delete_place)}").apply {
            setOk(getString(R.string.delete))
            setOnOkClickListener(View.OnClickListener {
                viewModel.delete(entity) {
                    Toast.makeText(context, R.string.msg_delete_success, Toast.LENGTH_SHORT).show()
                    viewModel.getData(intent.getIntExtra(EXTRA_CATEGORY_ID, 0), currentNativeAd)
                    isUpdated = true
                    dismiss()
                }
            })
            show()
        }
    }
}