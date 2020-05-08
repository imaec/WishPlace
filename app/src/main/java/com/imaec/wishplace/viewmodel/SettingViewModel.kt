package com.imaec.wishplace.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.utils.Utils

class SettingViewModel(context: Context) : BaseViewModel(context) {

    val appVersion = MutableLiveData<String>().set(Utils.getVersion(context))

}