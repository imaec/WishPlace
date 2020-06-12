package com.imaec.wishplace.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.imaec.wishplace.R
import com.imaec.wishplace.ui.view.dialog.ProgressDialog

abstract class BaseActivity<T : ViewDataBinding>(@LayoutRes private val layoutResId: Int) : AppCompatActivity() {

    protected val TAG = this::class.java.simpleName

    private val progressDialog: ProgressDialog by lazy { ProgressDialog(this) }
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutResId)

        init()

        FirebaseCrashlytics.getInstance().log("$TAG onCreate")
        FirebaseCrashlytics.getInstance().setCustomKey(getString(R.string.key_activity), TAG)
    }

    override fun onResume() {
        super.onResume()
        FirebaseCrashlytics.getInstance().log("$TAG onResume")
    }

    override fun onPause() {
        super.onPause()
        FirebaseCrashlytics.getInstance().log("$TAG onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseCrashlytics.getInstance().log("$TAG onDestroy")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        FirebaseCrashlytics.getInstance().log("$TAG onActivityResult")
    }

    private fun init() {
        MobileAds.initialize(this) {}
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

    protected fun <T : ViewModel> getViewModel(modelClass: Class<T>, vararg repository: Any) : T {
        return ViewModelProvider(this, BaseViewModelFactory(*repository)).get(modelClass)
    }

    protected fun getTransitionOption(view: View) : ActivityOptionsCompat {
        val p1 = Pair.create(view, view.transitionName)
        return ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1)
    }

    protected fun updateStatusBarColor(color: Int) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color
    }

    protected fun showProgress() {
        if (!progressDialog.isShowing) progressDialog.show()
    }

    protected fun hideProgress() {
        if (progressDialog.isShowing) progressDialog.dismiss()
    }

    protected fun logEvent(key: String, bundle: Bundle) {
        firebaseAnalytics.logEvent(key, bundle)
    }
}