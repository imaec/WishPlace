package com.imaec.wishplace.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
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
            editName.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    this@EditActivity.viewModel.liveTitle.value = p0.toString()
                }
            })
            editAddr.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    this@EditActivity.viewModel.liveAddress.value = p0.toString()
                }
            })
            editSite.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    this@EditActivity.viewModel.liveSite.value = p0.toString()
                }
            })
        }

        viewModel.apply {
            setData(
                intent.getStringExtra(EXTRA_TITLE) ?: "제목이 없습니다.",
                intent.getStringExtra(EXTRA_ADDRESS) ?: "주소가 없습니다.",
                intent.getStringExtra(EXTRA_IMG_URL) ?: "",
                intent.getStringExtra(EXTRA_SITE_URL) ?: "사이트가 없습니다.",
                intent.getBooleanExtra(EXTRA_IS_VISIT, false)
            )
            getData(intent.getIntExtra(EXTRA_PLACE_ID, 0))
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(0, 0)
    }

    fun onClick(view: View) {
        if (view.id == R.id.image_back) {
            onBackPressed()
        } else if (view.id == R.id.text_save) {
            viewModel.livePlace.value?.let {
                val entity = it
                viewModel.apply {
                    entity.apply {
                        name = liveTitle.value ?: "제목이 없습니다."
                        address = liveAddress.value ?: "주소가 없습니다."
                        siteUrl = liveSite.value ?: "사이트가 없습니다."
                    }
                    update(entity) { isSuccess ->
                        if (isSuccess) {
                            Toast.makeText(this@EditActivity, R.string.msg_edit_place_success, Toast.LENGTH_SHORT).show()
                            setResult(RESULT_EDIT, Intent().apply {
                                putExtra(EXTRA_IS_UPDATED, true)
                            })
                            onBackPressed()
                        } else {
                            Toast.makeText(this@EditActivity, R.string.msg_edit_place_fail, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } ?: run {
                Toast.makeText(this@EditActivity, R.string.msg_edit_place_fail, Toast.LENGTH_SHORT).show()
            }
        }
    }
}