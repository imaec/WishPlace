package com.imaec.wishplace.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.imaec.wishplace.*
import com.imaec.wishplace.base.BaseActivity
import com.imaec.wishplace.databinding.ActivityWriteBinding
import com.imaec.wishplace.room.entity.PlaceEntity
import com.imaec.wishplace.ui.view.dialog.CommonDialog
import com.imaec.wishplace.ui.view.dialog.InputDialog
import com.imaec.wishplace.viewmodel.WriteViewModel

class WriteActivity : BaseActivity<ActivityWriteBinding>(R.layout.activity_write) {

    private lateinit var viewModel: WriteViewModel
    private var categoryId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(WriteViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@WriteActivity
            viewModel = this@WriteActivity.viewModel
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
            viewModel.liveCategory.value = category
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
         }
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
            category = binding.textCategory.text.toString(),
            name = binding.editName.text.toString(),
            address = binding.editAddr.text.toString(),
            content = binding.editContent.text.toString(),
            siteUrl = binding.editSite.text.toString(),
            imageUrl = url
        )) {
            hideProgress()
            Toast.makeText(this, R.string.msg_write_place_success, Toast.LENGTH_SHORT).show()
            setResult(RESULT_WRITE)
            finish()
        }
    }
}