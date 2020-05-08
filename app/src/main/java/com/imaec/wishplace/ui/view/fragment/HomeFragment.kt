package com.imaec.wishplace.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.imaec.wishplace.*
import com.imaec.wishplace.base.BaseFragment
import com.imaec.wishplace.databinding.FragmentHomeBinding
import com.imaec.wishplace.model.PlaceDTO
import com.imaec.wishplace.ui.view.activity.DetailActivity
import com.imaec.wishplace.ui.view.activity.EditActivity
import com.imaec.wishplace.ui.view.activity.ListActivity
import com.imaec.wishplace.ui.view.dialog.EditDialog
import com.imaec.wishplace.viewmodel.HomeViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private lateinit var viewModel: HomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel(HomeViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@HomeFragment
            viewModel = this@HomeFragment.viewModel
        }

        viewModel.apply {
            addOnClickListener { item ->
                if (item is PlaceDTO) {
                    startActivityForResult(Intent(context, DetailActivity::class.java).apply {
                        putExtra(EXTRA_TITLE, item.title)
                        putExtra(EXTRA_ADDRESS, item.address)
                        putExtra(EXTRA_IMG_URL, item.imgUrl)
                        putExtra(EXTRA_SITE_URL, item.site)
                        putExtra(EXTRA_IS_VISIT, item.isVisit)
                    }, 0)
                } else if (item is String) {
                    // start ListActivity
                    startActivityForResult(Intent(context, ListActivity::class.java).apply {
                        putExtra(EXTRA_CATEGORY, item)
                    }, 0)
                }
            }
            addOnLongClickListener {dto ->
                val dialog = EditDialog(context!!).apply {
                    setTitle(dto.title)
                    setOnEditClickListener(View.OnClickListener {
                        startActivityForResult(Intent(context, EditActivity::class.java).apply {
                            putExtra(EXTRA_TITLE, dto.title)
                            putExtra(EXTRA_ADDRESS, dto.address)
                            putExtra(EXTRA_IMG_URL, dto.imgUrl)
                            putExtra(EXTRA_SITE_URL, dto.site)
                            putExtra(EXTRA_IS_VISIT, dto.isVisit)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            RESULT_EDIT -> {
                Log.d(TAG, "EDIT")
            }
            RESULT_DELETE -> {
                Log.d(TAG, "DELETE")
            }
        }
    }
}