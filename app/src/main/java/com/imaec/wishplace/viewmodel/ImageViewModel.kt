package com.imaec.wishplace.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.imaec.wishplace.base.BaseViewModel

class ImageViewModel(context: Context) : BaseViewModel(context) {

    val liveImgUrl = MutableLiveData<String>().set("")
}