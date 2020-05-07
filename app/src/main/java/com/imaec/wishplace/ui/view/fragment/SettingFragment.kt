package com.imaec.wishplace.ui.view.fragment

import android.os.Bundle
import android.view.View
import com.imaec.wishplace.R
import com.imaec.wishplace.base.BaseFragment
import com.imaec.wishplace.databinding.FragmentSettingBinding
import com.imaec.wishplace.viewmodel.SettingViewModel

class SettingFragment : BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    private lateinit var viewModel: SettingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel(SettingViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@SettingFragment
//            viewModel = this@SearchFragment.viewModel
        }
    }
}