package com.imaec.wishplace.ui.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.imaec.wishplace.R
import com.imaec.wishplace.base.BaseFragment
import com.imaec.wishplace.databinding.FragmentSearchResultBinding
import com.imaec.wishplace.viewmodel.SearchViewModel

class SearchResultFragment : BaseFragment<FragmentSearchResultBinding>(R.layout.fragment_search_result) {

    private lateinit var viewModel: SearchViewModel

    var keyword = ""
    var option = ""

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel(SearchViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@SearchResultFragment
            viewModel = this@SearchResultFragment.viewModel
        }

        arguments?.let {
            keyword = it.getString("keyword") ?: ""
            option = it.getString("option") ?: ""
            binding.textKeywordInfo.text = "'$keyword'에 대한 검색결과"
        }

        viewModel.search(keyword, option) {
            // onFail
        }
    }
}