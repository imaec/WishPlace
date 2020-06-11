package com.imaec.wishplace.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.imaec.wishplace.*
import com.imaec.wishplace.R
import com.imaec.wishplace.base.BaseFragment
import com.imaec.wishplace.databinding.FragmentHomeBinding
import com.imaec.wishplace.repository.PlaceRepository
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.PlaceDao
import com.imaec.wishplace.room.entity.CategoryEntity
import com.imaec.wishplace.room.entity.PlaceEntity
import com.imaec.wishplace.ui.util.HomeItemDecoration
import com.imaec.wishplace.ui.view.activity.DetailActivity
import com.imaec.wishplace.ui.view.activity.EditActivity
import com.imaec.wishplace.ui.view.activity.ListActivity
import com.imaec.wishplace.ui.view.dialog.CommonDialog
import com.imaec.wishplace.ui.view.dialog.EditDialog
import com.imaec.wishplace.viewmodel.HomeViewModel
import java.util.*

var currentNativeAd: UnifiedNativeAd? = null

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private lateinit var viewModel: HomeViewModel
    private lateinit var placeDao: PlaceDao
    private lateinit var placeRepository: PlaceRepository
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var itemDecoration: HomeItemDecoration
    private lateinit var interstitialAd: InterstitialAd

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        viewModel = getViewModel(HomeViewModel::class.java, placeRepository)

        binding.apply {
            lifecycleOwner = this@HomeFragment
            viewModel = this@HomeFragment.viewModel
            recyclerHome.layoutManager = gridLayoutManager
            recyclerHome.addItemDecoration(itemDecoration)
        }

        viewModel.apply {
            addOnClickListener { entity, view ->
                if (entity is PlaceEntity) {
                    showAd {
                        startActivityForResult(Intent(context, DetailActivity::class.java).apply {
                            putExtra(EXTRA_PLACE_ID, entity.placeId)
                            putExtra(EXTRA_CATEGORY, entity.category)
                            putExtra(EXTRA_TITLE, entity.name)
                            putExtra(EXTRA_ADDRESS, entity.address)
                            putExtra(EXTRA_IMG_URL, entity.imageUrl)
                            putExtra(EXTRA_SITE_URL, entity.siteUrl)
                            putExtra(EXTRA_IS_VISIT, entity.visitFlag)
                        }, 0, getTransitionOption(view).toBundle())
                    }
                } else if (entity is CategoryEntity) {
                    startActivityForResult(Intent(context, ListActivity::class.java).apply {
                        putExtra(EXTRA_CATEGORY_ID, entity.categoryId)
                        putExtra(EXTRA_CATEGORY, entity.category)
                    }, 0)
                }
            }
            addOnLongClickListener { entity ->
                val dialog = EditDialog(context!!).apply {
                    setTitle(entity.name)
                    setOnEditClickListener(View.OnClickListener {
                        startActivityForResult(Intent(context, EditActivity::class.java).apply {
                            putExtra(EXTRA_PLACE_ID, entity.placeId)
                            putExtra(EXTRA_CATEGORY, entity.category)
                            putExtra(EXTRA_TITLE, entity.name)
                            putExtra(EXTRA_ADDRESS, entity.address)
                            putExtra(EXTRA_CONTENT, entity.content)
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
            getData(currentNativeAd)
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
                viewModel.getData(currentNativeAd)
            }
        }
    }

    private fun init() {
        initNativeAd()

        placeDao = AppDatabase.getInstance(context!!).placeDao()
        placeRepository = PlaceRepository.getInstance(placeDao)
        gridLayoutManager =
            GridLayoutManager(context, 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (viewModel.adapter.getItemViewType(position)) {
                            TYPE_CATEGORY -> 2
                            TYPE_ITEM -> 1
                            else -> 1
                        }
                    }
                }
            }
        itemDecoration = HomeItemDecoration(context!!)
    }

    private fun initNativeAd() {
        val builder = AdLoader.Builder(context, getString(R.string.ad_id_home_native))
        builder.forUnifiedNativeAd { unifiedNativeAd ->
            currentNativeAd?.destroy()
            currentNativeAd = unifiedNativeAd

            viewModel.getData(currentNativeAd)
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
                Toast.makeText(context, "Failed to load native ad: $errorCode", Toast.LENGTH_SHORT).show()
            }
        }).build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun delete(entity: PlaceEntity) {
        CommonDialog(context!!, "'${entity.name}' ${getString(R.string.msg_delete_place)}").apply {
            setOk(getString(R.string.delete))
            setOnOkClickListener(View.OnClickListener {
                viewModel.delete(entity) {
                    Toast.makeText(context, R.string.msg_delete_success, Toast.LENGTH_SHORT).show()
                    viewModel.getData(currentNativeAd)
                    dismiss()
                }
            })
            show()
        }
    }

    private fun showAd(callback: () -> Unit) {
        Random().let {
            val ran = it.nextInt(4) + 1
            if (ran == 1) {
                showProgress()

                interstitialAd = InterstitialAd(context).apply {
                    adUnitId =  getString(R.string.ad_id_detail_front)
                    adListener = object : AdListener() {
                        override fun onAdLoaded() {
                            interstitialAd.show()
                        }

                        override fun onAdFailedToLoad(p0: Int) {
                            super.onAdFailedToLoad(p0)

                            Log.d(TAG, "    ## ad failed to load : $p0")
                            hideProgress()
                            callback()
                        }

                        override fun onAdClosed() {
                            super.onAdClosed()

                            hideProgress()
                            callback()
                        }
                    }
                }
                interstitialAd.loadAd(AdRequest.Builder().build())
            } else {
                callback()
            }
        }
    }

    fun notifyItemAdded() {
        viewModel.getData(currentNativeAd)
    }
}