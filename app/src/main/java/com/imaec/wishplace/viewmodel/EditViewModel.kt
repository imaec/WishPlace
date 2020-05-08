package com.imaec.wishplace.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.imaec.wishplace.base.BaseViewModel

class EditViewModel(context: Context) : BaseViewModel(context) {

    val liveTitle = MutableLiveData<String>()
    val liveAddress = MutableLiveData<String>()
    val liveImgUrl = MutableLiveData<String>()
    val liveSite = MutableLiveData<String>()
    val liveIsVisit = MutableLiveData<Boolean>()

    fun setData(title: String, address: String, imgUrl: String, site: String, isVisit: Boolean) {
        liveTitle.value = title
        liveAddress.value = address
        liveImgUrl.value = imgUrl
        liveSite.value = site
        liveIsVisit.value = isVisit
    }
}