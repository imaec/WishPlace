package com.imaec.wishplace.ui.view.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.imaec.wishplace.R
import com.imaec.wishplace.base.BaseActivity
import com.imaec.wishplace.databinding.ActivityLicenseBinding
import com.imaec.wishplace.viewmodel.LicenseViewModel

class LicenseActivity : BaseActivity<ActivityLicenseBinding>(R.layout.activity_license) {

    private lateinit var viewModel: LicenseViewModel
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()

        viewModel = getViewModel(LicenseViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@LicenseActivity
            viewModel = this@LicenseActivity.viewModel
            recyclerLicense.layoutManager = this@LicenseActivity.layoutManager
        }

        viewModel.getData()
    }

    private fun init() {
        layoutManager = LinearLayoutManager(this)
    }
}