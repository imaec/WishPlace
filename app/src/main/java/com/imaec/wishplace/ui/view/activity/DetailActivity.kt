package com.imaec.wishplace.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.imaec.wishplace.*
import com.imaec.wishplace.base.BaseActivity
import com.imaec.wishplace.databinding.ActivityDetailBinding
import com.imaec.wishplace.repository.PlaceRepository
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.PlaceDao
import com.imaec.wishplace.ui.view.dialog.CommonDialog
import com.imaec.wishplace.viewmodel.DetailViewModel
import com.kakao.kakaolink.v2.KakaoLinkResponse
import com.kakao.kakaolink.v2.KakaoLinkService
import com.kakao.network.ErrorResult
import com.kakao.network.callback.ResponseCallback

class DetailActivity : BaseActivity<ActivityDetailBinding>(R.layout.activity_detail) {

    private lateinit var viewModel: DetailViewModel
    private lateinit var placeDao: PlaceDao
    private lateinit var placeRepository: PlaceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()

        viewModel = getViewModel(DetailViewModel::class.java, placeRepository)

        binding.apply {
            lifecycleOwner = this@DetailActivity
            viewModel = this@DetailActivity.viewModel
        }

        viewModel.apply {
            setData(
                intent.getStringExtra(EXTRA_TITLE) ?: "제목이 없습니다.",
                intent.getStringExtra(EXTRA_ADDRESS) ?: "주소가 없습니다.",
                intent.getStringExtra(EXTRA_CONTENT) ?: "",
                intent.getStringExtra(EXTRA_IMG_URL) ?: "",
                intent.getStringExtra(EXTRA_SITE_URL) ?: "사이트가 없습니다.",
                intent.getBooleanExtra(EXTRA_IS_VISIT, false)
            )
            getData(intent.getIntExtra(EXTRA_PLACE_ID, 0))
            liveIsVisit.observe(this@DetailActivity, Observer {
                Glide.with(this@DetailActivity)
                    .load(if (it) R.drawable.ic_visit else R.drawable.ic_novisit)
                    .error(R.drawable.img_empty)
                    .into(binding.imageIsVisit)
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_EDIT) {
            Toast.makeText(this, R.string.msg_edit_place_success, Toast.LENGTH_SHORT).show()
            viewModel.getData(intent.getIntExtra(EXTRA_PLACE_ID, 0))
            data?.let {
                viewModel.isUpdated = it.getBooleanExtra(EXTRA_IS_UPDATED, false)
            }
        }
    }

    override fun onBackPressed() {
        if (viewModel.isUpdated) {
            setResult(RESULT_EDIT, Intent().apply {
                putExtra(EXTRA_IS_UPDATED, true)
            })
        }
        super.onBackPressed()
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.image_thumb -> {
                startActivity(Intent(this, ImageActivity::class.java).apply {
                    putExtra(EXTRA_IMG_URL, viewModel.liveImgUrl.value)
                })
            }
            R.id.image_edit -> {
                showPopup(view)
            }
            R.id.image_is_visit -> {
                viewModel.updateVisit { isSuccess ->
                    if (isSuccess) {
                        viewModel.isUpdated = true
                        viewModel.getData(intent.getIntExtra(EXTRA_PLACE_ID, 0))
                    } else {
                        Toast.makeText(this, R.string.msg_retry, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.text_share -> {
                KakaoLinkService.getInstance()
                    .sendCustom(this, getString(R.string.template_id_detail), viewModel.getArgs(), object : ResponseCallback<KakaoLinkResponse>() {
                        override fun onSuccess(result: KakaoLinkResponse?) {

                        }

                        override fun onFailure(errorResult: ErrorResult?) {
                            Log.e(TAG, errorResult.toString())
                            Toast.makeText(this@DetailActivity, R.string.msg_share_fail, Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
    }

    private fun init() {
        placeDao = AppDatabase.getInstance(this).placeDao()
        placeRepository = PlaceRepository.getInstance(placeDao)
    }

    private fun showPopup(view: View) {
        PopupMenu(this, view).apply {
            menuInflater.inflate(R.menu.menu_edit, menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.option_edit -> {
                        startActivityForResult(Intent(this@DetailActivity, EditActivity::class.java).apply {
                            putExtra(EXTRA_PLACE_ID, intent.getIntExtra(EXTRA_PLACE_ID, 0))
                            putExtra(EXTRA_TITLE, viewModel.liveTitle.value)
                            putExtra(EXTRA_ADDRESS, viewModel.liveAddress.value)
                            putExtra(EXTRA_IMG_URL, viewModel.liveImgUrl.value)
                            putExtra(EXTRA_SITE_URL, viewModel.liveSite.value)
                            putExtra(EXTRA_CONTENT, viewModel.liveContent.value)
                            putExtra(EXTRA_IS_VISIT, viewModel.liveIsVisit.value)
                        }, 0)
                        overridePendingTransition(0, 0)
                        true
                    }
                    R.id.option_delete -> {
                        delete()
                        true
                    }
                    else -> false
                }
            }
            show()
        }
    }

    private fun delete() {
        CommonDialog(this, "'${viewModel.liveTitle.value}' ${getString(R.string.msg_delete_place)}").apply {
            setOk(getString(R.string.delete))
            setOnOkClickListener(View.OnClickListener {
                viewModel.delete(viewModel.livePlace.value) { isSuccess ->
                    dismiss()

                    if (isSuccess) {
                        Toast.makeText(this@DetailActivity, R.string.msg_delete_success, Toast.LENGTH_SHORT).show()
                        setResult(RESULT_DELETE, Intent().apply {
                            putExtra(EXTRA_IS_UPDATED, true)
                        })
                        finish()
                    } else {
                        Toast.makeText(this@DetailActivity, R.string.msg_delete_fail, Toast.LENGTH_SHORT).show()
                    }
                }
            })
            show()
        }
    }
}