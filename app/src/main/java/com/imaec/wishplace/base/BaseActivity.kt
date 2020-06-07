package com.imaec.wishplace.base

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.imaec.wishplace.ui.view.dialog.ProgressDialog

abstract class BaseActivity<T : ViewDataBinding>(@LayoutRes private val layoutResId: Int) : AppCompatActivity() {

    protected val TAG = this::class.java.simpleName
    private val progressDialog: ProgressDialog by lazy { ProgressDialog(this) }

    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutResId)
    }

    protected fun <T : ViewModel> getViewModel(modelClass: Class<T>, vararg repository: Any) : T {
        return ViewModelProvider(this, BaseViewModelFactory(repository)).get(modelClass)
    }

    protected fun getTransitionOption(view: View) : ActivityOptionsCompat {
        val p1 = Pair.create(view, view.transitionName)
        return ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1)
    }

    protected fun showProgress() {
        if (!progressDialog.isShowing) progressDialog.show()
    }

    protected fun hideProgress() {
        if (progressDialog.isShowing) progressDialog.dismiss()
    }
}