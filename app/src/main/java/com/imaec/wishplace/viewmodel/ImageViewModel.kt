package com.imaec.wishplace.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.imaec.wishplace.base.BaseViewModel

class ImageViewModel : BaseViewModel() {

    private val _imgUrl = MutableLiveData<String>().set("")
    val imgUrl: LiveData<String>
        get() = _imgUrl

    fun setImgUrl(imgUrl: String) {
        _imgUrl.value = imgUrl
    }
}