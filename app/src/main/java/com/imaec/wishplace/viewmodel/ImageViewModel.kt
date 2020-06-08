package com.imaec.wishplace.viewmodel

import androidx.lifecycle.MutableLiveData
import com.imaec.wishplace.base.BaseViewModel

class ImageViewModel : BaseViewModel() {

    val liveImgUrl = MutableLiveData<String>().set("")
}