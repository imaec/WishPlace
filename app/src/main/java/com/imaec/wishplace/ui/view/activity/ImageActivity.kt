package com.imaec.wishplace.ui.view.activity

import android.os.Bundle
import com.imaec.wishplace.EXTRA_IMG_URL
import com.imaec.wishplace.R
import com.imaec.wishplace.base.BaseActivity
import com.imaec.wishplace.databinding.ActivityImageBinding
import com.imaec.wishplace.viewmodel.ImageViewModel

class ImageActivity : BaseActivity<ActivityImageBinding>(R.layout.activity_image) {

    private lateinit var viewModel: ImageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(ImageViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@ImageActivity
            viewModel = this@ImageActivity.viewModel
        }

        viewModel.liveImgUrl.value = intent.getStringExtra(EXTRA_IMG_URL) ?: ""
    }
}