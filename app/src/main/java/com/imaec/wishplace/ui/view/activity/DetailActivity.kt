package com.imaec.wishplace.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
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

        viewModel.setData(
            intent.getStringExtra(EXTRA_TITLE) ?: "제목이 없습니다.",
            intent.getStringExtra(EXTRA_ADDRESS) ?: "주소가 없습니다.",
            intent.getStringExtra(EXTRA_IMG_URL) ?: "",
            intent.getStringExtra(EXTRA_SITE_URL) ?: "사이트가 없습니다.",
            intent.getBooleanExtra(EXTRA_IS_VISIT, false)
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_EDIT) {
            Toast.makeText(this, "수정 완료!", Toast.LENGTH_SHORT).show()
        }
    }

    fun onClick(view: View) {
        if (view.id == R.id.image_edit) {
            showPopup(view)
        }
    }

    private fun showPopup(view: View) {
        PopupMenu(this, view).apply {
            menuInflater.inflate(R.menu.menu_edit, menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.option_edit -> {
                        startActivityForResult(Intent(this@DetailActivity, EditActivity::class.java).apply {
                            putExtra(EXTRA_TITLE, viewModel.liveTitle.value)
                            putExtra(EXTRA_ADDRESS, viewModel.liveAddress.value)
                            putExtra(EXTRA_IMG_URL, viewModel.liveImgUrl.value)
                            putExtra(EXTRA_SITE_URL, viewModel.liveSite.value)
                            putExtra(EXTRA_IS_VISIT, viewModel.liveIsVisit.value)
                        }, 0)
                        overridePendingTransition(0, 0)
                        true
                    }
                    R.id.option_delete -> {
                        Toast.makeText(this@DetailActivity, "삭제 되었습니다.", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_DELETE)
                        finish()
                        true
                    }
                    else -> false
                }
            }
            show()
        }
    }
}