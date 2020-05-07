package com.imaec.wishplace.ui.view.fragment

import android.os.Bundle
import android.view.View
import com.imaec.wishplace.R
import com.imaec.wishplace.base.BaseFragment
import com.imaec.wishplace.databinding.FragmentSearchBinding
import com.imaec.wishplace.viewmodel.SearchViewModel

class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private lateinit var viewModel: SearchViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel(SearchViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@SearchFragment
//            viewModel = this@SearchFragment.viewModel
        }
    }
}