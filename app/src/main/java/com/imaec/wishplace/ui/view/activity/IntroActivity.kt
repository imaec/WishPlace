package com.imaec.wishplace.ui.view.activity

import android.os.Bundle
import android.os.Handler
import android.view.animation.AlphaAnimation
import com.imaec.wishplace.R
import com.imaec.wishplace.base.BaseActivity
import com.imaec.wishplace.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity<ActivityIntroBinding>(R.layout.activity_intro) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val animation = AlphaAnimation(0f, 1f).apply {
            duration = 1100
        }
        binding.imageLogo.startAnimation(animation)

        Handler().postDelayed({
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }, 1300)
    }

    override fun onBackPressed() {
    }
}