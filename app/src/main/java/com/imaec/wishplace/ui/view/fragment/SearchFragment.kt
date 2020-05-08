package com.imaec.wishplace.ui.view.fragment

import android.os.Bundle
import android.view.View
import com.imaec.wishplace.R
import com.imaec.wishplace.base.BaseFragment
import com.imaec.wishplace.databinding.FragmentSearchBinding
import com.imaec.wishplace.ui.view.activity.MainActivity
import com.imaec.wishplace.viewmodel.SearchViewModel

class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private lateinit var viewModel: SearchViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel(SearchViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@SearchFragment
            viewModel = this@SearchFragment.viewModel
        }
    }

    fun onClick(view: View) {
        if (view.id == binding.imageSearch.id) {
            (activity as MainActivity?)?.let {
                it.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, it.fragmentSearchResult.apply { keyword = binding.editSearch.text.toString() })
                    .commitAllowingStateLoss()
            }
        }
    }
}