package com.imaec.wishplace.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.imaec.wishplace.*
import com.imaec.wishplace.base.BaseActivity
import com.imaec.wishplace.databinding.ActivityWriteBinding
import com.imaec.wishplace.model.NaverPlaceDTO
import com.imaec.wishplace.repository.CategoryRepository
import com.imaec.wishplace.repository.PlaceRepository
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.CategoryDao
import com.imaec.wishplace.room.dao.PlaceDao
import com.imaec.wishplace.room.entity.PlaceEntity
import com.imaec.wishplace.ui.util.NaverPlaceItemDecoration
import com.imaec.wishplace.ui.view.dialog.CommonDialog
import com.imaec.wishplace.ui.view.dialog.InputDialog
import com.imaec.wishplace.utils.KeyboardUtil
import com.imaec.wishplace.utils.SharedPreferenceManager
import com.imaec.wishplace.viewmodel.WriteViewModel
import kotlinx.android.synthetic.main.activity_write.*
import java.util.*

class WriteActivity : BaseActivity<ActivityWriteBinding>(R.layout.activity_write) {

    private lateinit var viewModel: WriteViewModel
    private lateinit var bottomSheet: BottomSheetBehavior<RecyclerView>
    private lateinit var categoryDao: CategoryDao
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var placeDao: PlaceDao
    private lateinit var placeRepository: PlaceRepository
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var itemDecoration: NaverPlaceItemDecoration
    private lateinit var interstitialAd: InterstitialAd

    private var categoryId = 0
    private var isRemoveAd = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()

        viewModel = getViewModel(WriteViewModel::class.java, categoryRepository, placeRepository)

        binding.apply {
            lifecycleOwner = this@WriteActivity
            viewModel = this@WriteActivity.viewModel
            recyclerBottomSheet.layoutManager = this@WriteActivity.layoutManager
            recyclerBottomSheet.addItemDecoration(itemDecoration)
            bottomSheet = BottomSheetBehavior.from(recyclerBottomSheet).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        viewModel.apply {
            addOnClickListener { item ->
                hideBottomSheet()
                if (item is NaverPlaceDTO.Item) {
                    binding.apply {
                        editName.setText(Html.fromHtml(item.title).toString())
                        editName.setSelection(editName.length())
                        editAddr.setText(item.roadAddress)

                        if (editSite.text.toString().isEmpty()) editSite.setText(item.link)
                    }
                }
            }
        }

        intent.getStringExtra(EXTRA_TITLE)?.let {
            binding.editName.setText(it)
        }
        intent.getStringExtra(EXTRA_ADDRESS)?.let {
            binding.editAddr.setText(it)
        }
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            binding.editSite.setText(it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            categoryId = data?.getIntExtra(EXTRA_CATEGORY_ID, 0) ?: 0
            val category = data?.getStringExtra(EXTRA_CATEGORY)
            viewModel.setCategory(category)
        }
    }

    override fun onBackPressed() {
        if (bottomSheet.state == BottomSheetBehavior.STATE_COLLAPSED) {
            hideBottomSheet()
        } else {
            super.onBackPressed()
        }
    }

    fun onClick(view: View) {
         when (view.id) {
             R.id.image_category_add -> {
                 InputDialog(this).apply {
                     setOnAddClickListener {
                         viewModel.addCategory(it) { isSuccess ->
                             Toast.makeText(context,
                                 if (isSuccess) { "'$it' " + context.getString(R.string.msg_category_added) } else "'$it' " + context.getString(R.string.msg_category_exist),
                                 Toast.LENGTH_SHORT
                             ).show()
                             dismiss()
                         }
                     }
                 }.show()
             }
             R.id.image_search_name -> {
                 showProgress()

                 val name = binding.editName.text.toString()

                 viewModel.getNaverPlace(name) { result ->
                     hideProgress()
                     when (result) {
                         NaverPlaceResult.SUCCESS -> {
                             bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
                             binding.viewBg.visibility = View.VISIBLE
                         }
                         NaverPlaceResult.FAIL_EMPTY_NAME, NaverPlaceResult.FAIL_UNKNWON -> {
                             Toast.makeText(this, result.msg, Toast.LENGTH_SHORT).show()
                         }
                         else -> {}
                     }
                 }
             }
             R.id.text_category -> {
                 startActivityForResult(Intent(this, CategorySelectActivity::class.java), 0)
             }
             R.id.text_save -> {
                 val category = binding.textCategory.text.toString()
                 val title = binding.editName.text.toString()
                 val address = binding.editAddr.text.toString()
                 val result = viewModel.validateData(category, title, address)
                 if (result == ValidateResult.SUCCESS) {
                     showProgress()
                     save()
                 } else {
                     Toast.makeText(this, result.msg, Toast.LENGTH_SHORT).show()
                 }
             }
             R.id.view_bg -> {
                 hideBottomSheet()
             }
         }
    }

    private fun init() {
        isRemoveAd = SharedPreferenceManager.getBool(this, SharedPreferenceManager.KEY.PREF_REMOVE_AD, false)

        categoryDao = AppDatabase.getInstance(this).categoryDao()
        categoryRepository = CategoryRepository.getInstance(categoryDao)
        placeDao = AppDatabase.getInstance(this).placeDao()
        placeRepository = PlaceRepository.getInstance(placeDao)
        layoutManager = LinearLayoutManager(this)
        itemDecoration = NaverPlaceItemDecoration(this)
    }

    private fun save() {
        val site = binding.editSite.text.toString()
        viewModel.checkUrl(site, { url ->
            save(url)
        }, {
            CommonDialog(this, if (it == null) getString(R.string.msg_image_empty) else getString(R.string.msg_url_empty)).apply {
                setOnOkClickListener(View.OnClickListener {
                    save("")
                    dismiss()
                })
                setOnCancelClickListener(View.OnClickListener {
                    hideProgress()
                    dismiss()
                })
                show()
            }
        })
    }

    private fun save(url: String) {
        viewModel.save(PlaceEntity(
            foreignId = categoryId,
            name = binding.editName.text.toString(),
            address = binding.editAddr.text.toString(),
            content = binding.editContent.text.toString(),
            siteUrl = binding.editSite.text.toString(),
            imageUrl = url
        )) {
            showAd {
                hideProgress()
                Toast.makeText(this, R.string.msg_write_place_success, Toast.LENGTH_SHORT).show()
                setResult(RESULT_WRITE)
                finish()
            }
        }
    }

    private fun hideBottomSheet() {
        if (bottomSheet.state != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
            view_bg.visibility = View.GONE
        }
    }

    private fun showAd(callback: () -> Unit) {
        if (isRemoveAd) {
            callback()
            return
        }

        Random().let {
            val ran = it.nextInt(4) + 1
            if (ran == 1) {
                showProgress()

                interstitialAd = InterstitialAd(this).apply {
                    adUnitId =  getString(R.string.ad_id_write_front)
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
}