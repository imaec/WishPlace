package com.imaec.wishplace.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.imaec.wishplace.R
import com.imaec.wishplace.ui.view.dialog.ProgressDialog

abstract class BaseFragment<T : ViewDataBinding>(@LayoutRes private val layoutResId: Int) : Fragment() {

    protected val TAG = this::class.java.simpleName

    private val progressDialog: ProgressDialog by lazy { ProgressDialog(context!!) }
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    protected lateinit var binding: T

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        FirebaseCrashlytics.getInstance().log("$TAG onViewCreated")
        FirebaseCrashlytics.getInstance().setCustomKey(getString(R.string.key_fragment), TAG)
        logEvent(getString(R.string.event_screen_fragment), Bundle().apply {
            putString(getString(R.string.key_screen_fragment), TAG)
        })
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
        MobileAds.initialize(context) {}
        firebaseAnalytics = FirebaseAnalytics.getInstance(context!!)
    }

    protected fun <T : ViewModel> getViewModel(modelClass: Class<T>, vararg repository: Any) : T {
        return ViewModelProvider(this, BaseViewModelFactory(*repository)).get(modelClass)
    }

    protected fun getTransitionOption(view: View) : ActivityOptionsCompat {
        val p1 = Pair.create(view, view.transitionName)
        return ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, p1)
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