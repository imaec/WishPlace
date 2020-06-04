package com.imaec.wishplace.ui.view.activity

import android.os.Bundle
import com.imaec.wishplace.R
import com.imaec.wishplace.base.BaseActivity
import com.imaec.wishplace.databinding.ActivityLicenseBinding
import com.imaec.wishplace.viewmodel.LicenseViewModel

class LicenseActivity : BaseActivity<ActivityLicenseBinding>(R.layout.activity_license) {

    private lateinit var viewModel: LicenseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(LicenseViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@LicenseActivity
            viewModel = this@LicenseActivity.viewModel
        }

        viewModel.getData()
    }
}