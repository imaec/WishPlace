package com.imaec.wishplace.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import android.widget.Toast
import com.imaec.wishplace.*
import com.imaec.wishplace.base.BaseFragment
import com.imaec.wishplace.databinding.FragmentSearchBinding
import com.imaec.wishplace.room.entity.KeywordEntity
import com.imaec.wishplace.ui.view.activity.EditActivity
import com.imaec.wishplace.ui.view.activity.MainActivity
import com.imaec.wishplace.ui.view.dialog.CommonDialog
import com.imaec.wishplace.utils.KeyboardUtil
import com.imaec.wishplace.utils.SharedPreferenceManager
import com.imaec.wishplace.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private lateinit var viewModel: SearchViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel(SearchViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@SearchFragment
            viewModel = this@SearchFragment.viewModel
            textOption.text = SharedPreferenceManager.getString(context!!, SharedPreferenceManager.KEY.PREF_SEARCH_OPTION, "이름")
            editSearch.setOnEditorActionListener { textView, i, keyEvent ->
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    search()
                    KeyboardUtil.hideKeyboardFrom(context!!)
                    return@setOnEditorActionListener true
                }
                false
            }
        }

        viewModel.apply {
            getKeyword()
            addOnClickListener { entity ->
                if (entity is KeywordEntity) {
                    val keyword = entity.keyword
                    val option = binding.textOption.text.toString()
                    search(keyword, option)
                }
            }
            addOnDeleteClickListener { entity ->
                delete(entity)
            }
            addOnAllDeleteClickListener {
                deleteAll()
            }
        }
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.image_search -> {
                search()
            }
            R.id.linear_option -> {
                showPopup(view)
            }
        }
    }

    private fun showPopup(view: View) {
        PopupMenu(context, view).apply {
            menuInflater.inflate(R.menu.menu_search_option, menu)
            setOnMenuItemClickListener {
                binding.textOption.text = (when (it.itemId) {
                    R.id.option_name -> getString(R.string.name)
                    R.id.option_address -> getString(R.string.address)
                    else -> getString(R.string.name)
                })

                SharedPreferenceManager.putValue(context!!, SharedPreferenceManager.KEY.PREF_SEARCH_OPTION, binding.textOption.text.toString())
                true
            }
            show()
        }
    }

    private fun search() {
        val keyword = binding.editSearch.text.toString()
        val option = binding.textOption.text.toString()
        binding.editSearch.setText("")

        if (keyword.isEmpty()) {
            Toast.makeText(context, R.string.msg_keyword_empty, Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.saveKeyword(KeywordEntity(keyword = keyword))

        search(keyword, option)
    }

    private fun search(keyword: String, option: String) {
        (activity as MainActivity?)?.let {
            it.isSearchResult = true
            it.supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame, it.fragmentSearchResult.apply {
                    arguments = Bundle().apply {
                        putString("keyword", keyword)
                        putString("option", option)
                    }
                })
                .commitAllowingStateLoss()
        }
    }

    private fun delete(entity: KeywordEntity) {
        CommonDialog(context!!, "검색어를 삭제하시겠습니까?").apply {
            setOnOkClickListener(View.OnClickListener {
                dismiss()
                viewModel.delete(entity) {
                    Toast.makeText(context, R.string.msg_delete_success, Toast.LENGTH_SHORT).show()
                    viewModel.getKeyword()
                }
            })
            show()
        }
    }

    private fun deleteAll() {
        CommonDialog(context!!, "검색어를 모두 삭제하시겠습니까?").apply {
            setOnOkClickListener(View.OnClickListener {
                dismiss()
                viewModel.deleteAll {
                    Toast.makeText(context, R.string.msg_delete_success, Toast.LENGTH_SHORT).show()
                    viewModel.getKeyword()
                }
            })
            show()
        }
    }
}