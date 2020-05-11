package com.imaec.wishplace.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.imaec.wishplace.*
import com.imaec.wishplace.base.BaseActivity
import com.imaec.wishplace.databinding.ActivityListBinding
import com.imaec.wishplace.model.PlaceDTO
import com.imaec.wishplace.room.entity.PlaceEntity
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
            addOnClickListener { item ->
                if (item is PlaceEntity) {
                    startActivityForResult(Intent(this@ListActivity, DetailActivity::class.java).apply {
                        putExtra(EXTRA_TITLE, item.name)
                        putExtra(EXTRA_ADDRESS, item.address)
                        putExtra(EXTRA_IMG_URL, item.imageUrl)
                        putExtra(EXTRA_SITE_URL, item.siteUrl)
                        putExtra(EXTRA_IS_VISIT, item.visitFlag)
                    }, 0)
                }
            }
            addOnLongClickListener { entity ->
                val dialog = EditDialog(this@ListActivity).apply {
                    setTitle(entity.name)
                    setOnEditClickListener(View.OnClickListener {
                        startActivityForResult(Intent(context, EditActivity::class.java).apply {
                            putExtra(EXTRA_TITLE, entity.name)
                            putExtra(EXTRA_ADDRESS, entity.address)
                            putExtra(EXTRA_IMG_URL, entity.imageUrl)
                            putExtra(EXTRA_SITE_URL, entity.siteUrl)
                            putExtra(EXTRA_IS_VISIT, entity.visitFlag)
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