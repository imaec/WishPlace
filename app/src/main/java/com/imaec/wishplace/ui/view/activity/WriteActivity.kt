package com.imaec.wishplace.ui.view.activity

import android.os.Bundle
import com.imaec.wishplace.R
import com.imaec.wishplace.base.BaseActivity
import com.imaec.wishplace.databinding.ActivityWriteBinding
import com.imaec.wishplace.viewmodel.WriteViewModel

class WriteActivity : BaseActivity<ActivityWriteBinding>(R.layout.activity_write) {

    private lateinit var viewModel: WriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(WriteViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@WriteActivity
            viewModel = this@WriteActivity.viewModel
        }
    }
}