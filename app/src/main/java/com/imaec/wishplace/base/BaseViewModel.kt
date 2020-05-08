package com.imaec.wishplace.base

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel(protected val context: Context) : ViewModel() {

    protected val TAG = this::class.java.simpleName

    open fun onClick(view: View) {

    }

    fun MutableLiveData<ArrayList<Any>>.set(value: ArrayList<Any>) : MutableLiveData<ArrayList<Any>> {
        this.value = value
        return this
    }

    fun MutableLiveData<String>.set(value: String) : MutableLiveData<String> {
        this.value = value
        return this
    }
}