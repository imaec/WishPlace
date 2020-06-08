package com.imaec.wishplace.viewmodel

import androidx.lifecycle.MutableLiveData
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.ui.adapter.LicenseAdapter

@Suppress("UNCHECKED_CAST")
class LicenseViewModel : BaseViewModel() {

    init {
        adapter = LicenseAdapter()
    }

    val liveListLicense = MutableLiveData<ArrayList<Any>>().set(ArrayList())

    fun getData() {
        val list = arrayListOf(
            "Icon made by Roundicons from www.flaticon.com",
            "Icon made by Freepik from www.flaticon.com",
            "Icon made by Those Icons from www.flaticon.com",
            "Icon made by Kiranshastry from www.flaticon.com",
            "Icon made by bqlqn from www.flaticon.com"
        )
        liveListLicense.value = list as ArrayList<Any>
    }
}