package com.imaec.wishplace.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import com.imaec.wishplace.*
import com.imaec.wishplace.base.BaseFragment
import com.imaec.wishplace.databinding.FragmentSearchBinding
import com.imaec.wishplace.ui.view.activity.EditActivity
import com.imaec.wishplace.ui.view.activity.MainActivity
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
        }
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.image_search -> {
                (activity as MainActivity?)?.let {
                    it.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame, it.fragmentSearchResult.apply {
                            arguments = Bundle().apply {
                                putString("keyword", binding.editSearch.text.toString())
                                putString("option", binding.textOption.text.toString())
                            }
                        })
                        .commitAllowingStateLoss()
                }
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
}