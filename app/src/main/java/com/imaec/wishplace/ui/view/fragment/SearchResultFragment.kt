package com.imaec.wishplace.ui.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.imaec.wishplace.*
import com.imaec.wishplace.base.BaseFragment
import com.imaec.wishplace.databinding.FragmentSearchResultBinding
import com.imaec.wishplace.room.entity.CategoryEntity
import com.imaec.wishplace.room.entity.PlaceEntity
import com.imaec.wishplace.ui.view.activity.DetailActivity
import com.imaec.wishplace.ui.view.activity.EditActivity
import com.imaec.wishplace.ui.view.activity.ListActivity
import com.imaec.wishplace.ui.view.dialog.EditDialog
import com.imaec.wishplace.viewmodel.SearchResultViewModel
import com.imaec.wishplace.viewmodel.SearchViewModel

class SearchResultFragment : BaseFragment<FragmentSearchResultBinding>(R.layout.fragment_search_result) {

    private lateinit var viewModel: SearchResultViewModel

    var keyword = ""
    var option = ""

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel(SearchResultViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@SearchResultFragment
            viewModel = this@SearchResultFragment.viewModel
        }

        arguments?.let {
            keyword = it.getString("keyword") ?: ""
            option = it.getString("option") ?: ""
            binding.textKeywordInfo.text = "'$keyword'에 대한 검색결과"
        }

        viewModel.apply {
            search(keyword, option) {

            }
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
                        delete(entity) {
                            Toast.makeText(context, R.string.msg_delete_success, Toast.LENGTH_SHORT).show()
                            search(keyword, option) {  }
                            dismiss()
                        }
                    })
                }
                dialog.show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            RESULT_EDIT, RESULT_DELETE -> viewModel.search(keyword, option) {  }
        }
    }
}