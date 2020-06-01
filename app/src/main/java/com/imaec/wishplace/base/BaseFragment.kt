package com.imaec.wishplace.base

import android.os.Bundle
import android.util.Log
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
import com.imaec.wishplace.ui.view.dialog.ProgressDialog

abstract class BaseFragment<T : ViewDataBinding>(@LayoutRes private val layoutResId: Int) : Fragment() {

    protected val TAG = this::class.java.simpleName
    private val progressDialog: ProgressDialog by lazy { ProgressDialog(context!!) }

    protected lateinit var binding: T

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        return binding.root
    }

    protected fun <T : ViewModel> getViewModel(modelClass: Class<T>) : T {
        return ViewModelProvider(this, BaseViewModelFactory(context!!)).get(modelClass)
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
}