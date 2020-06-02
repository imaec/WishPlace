package com.imaec.wishplace.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.imaec.wishplace.*
import com.imaec.wishplace.base.BaseFragment
import com.imaec.wishplace.databinding.FragmentHomeBinding
import com.imaec.wishplace.model.PlaceDTO
import com.imaec.wishplace.room.entity.CategoryEntity
import com.imaec.wishplace.room.entity.PlaceEntity
import com.imaec.wishplace.ui.view.activity.DetailActivity
import com.imaec.wishplace.ui.view.activity.EditActivity
import com.imaec.wishplace.ui.view.activity.ListActivity
import com.imaec.wishplace.ui.view.dialog.CommonDialog
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
            addOnClickListener { entity, view ->
                if (entity is PlaceEntity) {
                    startActivityForResult(Intent(context, DetailActivity::class.java).apply {
                        putExtra(EXTRA_PLACE_ID, entity.placeId)
                        putExtra(EXTRA_TITLE, entity.name)
                        putExtra(EXTRA_ADDRESS, entity.address)
                        putExtra(EXTRA_IMG_URL, entity.imageUrl)
                        putExtra(EXTRA_SITE_URL, entity.siteUrl)
                        putExtra(EXTRA_IS_VISIT, entity.visitFlag)
                    }, 0, getTransitionOption(view).toBundle())
                } else if (entity is CategoryEntity) {
                    startActivityForResult(Intent(context, ListActivity::class.java).apply {
                        putExtra(EXTRA_CATEGORY_ID, entity.categoryId)
                        putExtra(EXTRA_CATEGORY, entity.category)
                    }, 0)
                }
            }
            addOnLongClickListener { entity ->
                val dialog = EditDialog(context!!).apply {
                    setTitle(entity.name)
                    setOnEditClickListener(View.OnClickListener {
                        startActivityForResult(Intent(context, EditActivity::class.java).apply {
                            putExtra(EXTRA_PLACE_ID, entity.placeId)
                            putExtra(EXTRA_TITLE, entity.name)
                            putExtra(EXTRA_ADDRESS, entity.address)
                            putExtra(EXTRA_CONTENT, entity.content)
                            putExtra(EXTRA_IMG_URL, entity.imageUrl)
                            putExtra(EXTRA_SITE_URL, entity.siteUrl)
                            putExtra(EXTRA_CONTENT, entity.content)
                            putExtra(EXTRA_IS_VISIT, entity.visitFlag)
                        }, 0)
                        dismiss()
                    })
                    setOnDeleteClickListener(View.OnClickListener {
                        delete(entity)
                        dismiss()
                    })
                }
                dialog.show()
            }
            getData {  }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            RESULT_EDIT, RESULT_DELETE -> {
                viewModel.getData {  }
            }
        }
    }

    private fun delete(entity: PlaceEntity) {
        CommonDialog(context!!, "'${entity.name}' ${getString(R.string.msg_delete_place)}").apply {
            setOk(getString(R.string.delete))
            setOnOkClickListener(View.OnClickListener {
                viewModel.delete(entity) {
                    Toast.makeText(context, R.string.msg_delete_success, Toast.LENGTH_SHORT).show()
                    viewModel.getData {  }
                    dismiss()
                }
            })
            show()
        }
    }

    fun notifyItemAdded() {
        viewModel.getData {  }
    }
}