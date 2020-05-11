package com.imaec.wishplace.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.imaec.wishplace.EXTRA_CATEGORY
import com.imaec.wishplace.EXTRA_CATEGORY_ID
import com.imaec.wishplace.R
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

        viewModel.apply {

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
                 if (binding.editSite.text.isEmpty()) {
                     return
                 }
                 val site = binding.editSite.text.toString()
                 viewModel.checkUrl(site) { isSuccess, url ->
                     Log.d(TAG, "$isSuccess")
                     if (isSuccess) {
                         viewModel.save(PlaceEntity(
                             foreignId = categoryId,
                             category = binding.textCategory.text.toString(),
                             name = binding.editName.text.toString(),
                             address = binding.editAddr.text.toString(),
                             siteUrl = binding.editSite.text.toString(),
                             imageUrl = url
                         )) {
                             Toast.makeText(this, R.string.msg_write_place_success, Toast.LENGTH_SHORT).show()
                         }
                     } else {
                         CommonDialog(this, getString(R.string.msg_image_empty)).apply {
                             setOnOkClickListener(View.OnClickListener {
                                 // viewModel.save()
                             })
                         }
                     }
                 }
             }
         }
    }
}