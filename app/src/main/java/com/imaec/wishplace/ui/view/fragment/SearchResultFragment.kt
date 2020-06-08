package com.imaec.wishplace.ui.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.imaec.wishplace.*
import com.imaec.wishplace.base.BaseFragment
import com.imaec.wishplace.databinding.FragmentSearchResultBinding
import com.imaec.wishplace.repository.PlaceRepository
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.PlaceDao
import com.imaec.wishplace.room.entity.CategoryEntity
import com.imaec.wishplace.room.entity.PlaceEntity
import com.imaec.wishplace.ui.util.PlaceItemDecoration
import com.imaec.wishplace.ui.view.activity.DetailActivity
import com.imaec.wishplace.ui.view.activity.EditActivity
import com.imaec.wishplace.ui.view.activity.ListActivity
import com.imaec.wishplace.ui.view.dialog.CommonDialog
import com.imaec.wishplace.ui.view.dialog.EditDialog
import com.imaec.wishplace.viewmodel.SearchResultViewModel

class SearchResultFragment : BaseFragment<FragmentSearchResultBinding>(R.layout.fragment_search_result) {

    private lateinit var viewModel: SearchResultViewModel
    private lateinit var placeDao: PlaceDao
    private lateinit var placeRepository: PlaceRepository
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var itemDecoration: PlaceItemDecoration

    var keyword = ""
    var option = ""

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        viewModel = getViewModel(SearchResultViewModel::class.java, placeRepository)

        binding.apply {
            lifecycleOwner = this@SearchResultFragment
            viewModel = this@SearchResultFragment.viewModel
            recyclerSearch.layoutManager = gridLayoutManager
            recyclerSearch.addItemDecoration(itemDecoration)
        }

        arguments?.let {
            keyword = it.getString("keyword") ?: ""
            option = it.getString("option") ?: ""

            val ss = SpannableString("'$keyword'에 대한 검색결과")
            val start = ss.toString().indexOf(keyword)
            val end = start + keyword.length
            ss.setSpan(ForegroundColorSpan(ContextCompat.getColor(context!!, R.color.colorAccent)), start, end, 0)
            binding.textKeywordInfo.text = ss
        }

        viewModel.apply {
            search(keyword, option) {  }
            addOnClickListener { entity, view ->
                if (entity is PlaceEntity) {
                    startActivityForResult(Intent(context, DetailActivity::class.java).apply {
                        putExtra(EXTRA_PLACE_ID, entity.placeId)
                        putExtra(EXTRA_CATEGORY, entity.category)
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
                            putExtra(EXTRA_CATEGORY, entity.category)
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
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            RESULT_EDIT, RESULT_DELETE -> {
                viewModel.search(keyword, option) {  }
            }
        }
    }

    private fun init() {
        placeDao = AppDatabase.getInstance(context!!).placeDao()
        placeRepository = PlaceRepository.getInstance(placeDao)
        gridLayoutManager = GridLayoutManager(context, 2)
        itemDecoration = PlaceItemDecoration(context!!)
    }

    private fun delete(entity: PlaceEntity) {
        CommonDialog(context!!, "'${entity.name}' ${getString(R.string.msg_delete_place)}").apply {
            setOk(getString(R.string.delete))
            setOnOkClickListener(View.OnClickListener {
                viewModel.delete(entity) {
                    Toast.makeText(context, R.string.msg_delete_success, Toast.LENGTH_SHORT).show()
                    viewModel.search(keyword, option) {  }
                    dismiss()
                }
            })
            show()
        }
    }
}