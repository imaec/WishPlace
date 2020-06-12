package com.imaec.wishplace.utils

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.imaec.wishplace.R

class AdRemoveHandler(private val activity: Activity) {

    private var clickedTime: Long = 0
    private var clickCount = 0

    fun onClick() {
        clickedTime = System.currentTimeMillis()
        if (System.currentTimeMillis() > clickedTime + 1000) {
            clickCount = 1
        } else if (System.currentTimeMillis() <= clickedTime + 1000) {
            clickCount++
            if (clickCount == 10) {
                val isRemoveAd = !SharedPreferenceManager.getBool(activity, SharedPreferenceManager.KEY.PREF_REMOVE_AD, false)
                SharedPreferenceManager.putValue(activity, SharedPreferenceManager.KEY.PREF_REMOVE_AD, isRemoveAd)
                Toast.makeText(activity, activity.getString(if (isRemoveAd) R.string.msg_remove_ad else R.string.msg_show_ad), Toast.LENGTH_SHORT).show()
            }
        }
    }
}