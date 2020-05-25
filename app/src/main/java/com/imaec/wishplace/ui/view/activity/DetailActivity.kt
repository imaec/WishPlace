package com.imaec.wishplace.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.imaec.wishplace.*
import com.imaec.wishplace.base.BaseActivity
import com.imaec.wishplace.databinding.ActivityDetailBinding
import com.imaec.wishplace.viewmodel.DetailViewModel

class DetailActivity : BaseActivity<ActivityDetailBinding>(R.layout.activity_detail) {

    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(DetailViewModel::class.java)

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
                binding.textVisit.setBackgroundResource(if (it) R.drawable.bg_circle_accent else R.drawable.bg_circle_gray)
                binding.textVisit.setTextColor(if (it) ContextCompat.getColor(this@DetailActivity, R.color.colorAccent) else ContextCompat.getColor(this@DetailActivity, R.color.gray))
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
            R.id.text_visit -> {
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

            }
        }
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
                        viewModel.delete(viewModel.livePlace.value) { isSuccess ->
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
                        true
                    }
                    else -> false
                }
            }
            show()
        }
    }
}