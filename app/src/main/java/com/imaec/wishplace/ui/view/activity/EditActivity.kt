package com.imaec.wishplace.ui.view.activity

import android.os.Bundle
import android.view.View
import com.imaec.wishplace.*
import com.imaec.wishplace.base.BaseActivity
import com.imaec.wishplace.databinding.ActivityEditBinding
import com.imaec.wishplace.viewmodel.EditViewModel

class EditActivity : BaseActivity<ActivityEditBinding>(R.layout.activity_edit) {

    private lateinit var viewModel: EditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(EditViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@EditActivity
            viewModel = this@EditActivity.viewModel
        }

        viewModel.setData(
            intent.getStringExtra(EXTRA_TITLE) ?: "제목이 없습니다.",
            intent.getStringExtra(EXTRA_ADDRESS) ?: "주소가 없습니다.",
            intent.getStringExtra(EXTRA_IMG_URL) ?: "",
            intent.getStringExtra(EXTRA_SITE_URL) ?: "사이트가 없습니다.",
            intent.getBooleanExtra(EXTRA_IS_VISIT, false)
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, 0)
    }

    fun onClick(view: View) {
        if (view.id == R.id.image_back) {
            onBackPressed()
        } else if (view.id == R.id.text_save) {
            setResult(RESULT_EDIT)
            onBackPressed()
        }
    }
}