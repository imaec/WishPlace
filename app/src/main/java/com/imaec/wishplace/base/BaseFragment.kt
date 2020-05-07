package com.imaec.wishplace.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class BaseFragment<T : ViewDataBinding>(@LayoutRes private val layoutResId: Int) : Fragment() {

    private val TAG = this::class.java.simpleName

    protected lateinit var binding: T

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        return binding.root
    }

    protected fun <T : ViewModel> getViewModel(modelClass: Class<T>) : T {
        return ViewModelProvider(this, BaseViewModelFactory(context!!)).get(modelClass)
    }
}