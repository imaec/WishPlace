package com.imaec.wishplace.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.ui.adapter.LicenseAdapter

@Suppress("UNCHECKED_CAST")
class LicenseViewModel(context: Context) : BaseViewModel(context) {

    init {
        adapter = LicenseAdapter()
    }

    val liveListLicense = MutableLiveData<ArrayList<Any>>().set(ArrayList())
    val layoutManager = LinearLayoutManager(context)

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