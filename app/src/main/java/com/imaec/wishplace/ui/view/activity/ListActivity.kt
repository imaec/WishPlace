package com.imaec.wishplace.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.imaec.wishplace.*
import com.imaec.wishplace.base.BaseActivity
import com.imaec.wishplace.databinding.ActivityListBinding
import com.imaec.wishplace.model.PlaceDTO
import com.imaec.wishplace.ui.view.dialog.EditDialog
import com.imaec.wishplace.viewmodel.ListViewModel

class ListActivity : BaseActivity<ActivityListBinding>(R.layout.activity_list) {

    private lateinit var viewModel: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(ListViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@ListActivity
            viewModel = this@ListActivity.viewModel
        }

        viewModel.apply {
            liveCategory.value = intent.getStringExtra(EXTRA_CATEGORY)
            addOnClickListener {  item ->
                if (item is PlaceDTO) {
                    startActivityForResult(Intent(this@ListActivity, DetailActivity::class.java).apply {
                        putExtra(EXTRA_TITLE, item.title)
                        putExtra(EXTRA_ADDRESS, item.address)
                        putExtra(EXTRA_IMG_URL, item.imgUrl)
                        putExtra(EXTRA_SITE_URL, item.site)
                        putExtra(EXTRA_IS_VISIT, item.isVisit)
                    }, 0)
                }
            }
            addOnLongClickListener { item ->
                val dialog = EditDialog(this@ListActivity).apply {
                    setTitle(item.title)
                    setOnEditClickListener(View.OnClickListener {
                        startActivityForResult(Intent(context, EditActivity::class.java).apply {
                            putExtra(EXTRA_TITLE, item.title)
                            putExtra(EXTRA_ADDRESS, item.address)
                            putExtra(EXTRA_IMG_URL, item.imgUrl)
                            putExtra(EXTRA_SITE_URL, item.site)
                            putExtra(EXTRA_IS_VISIT, item.isVisit)
                        }, 0)
                        dismiss()
                    })
                    setOnDeleteClickListener(View.OnClickListener {
                        dismiss()
                    })
                }
                dialog.show()
            }
        }
    }
}