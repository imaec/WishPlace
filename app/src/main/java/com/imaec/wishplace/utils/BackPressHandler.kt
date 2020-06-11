package com.imaec.wishplace.utils

import android.app.Activity
import android.widget.Toast

class BackPressHandler(private val activity: Activity) {

    private var backKeyPressedTime: Long = 0
    private var toast: Toast? = null

    fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis()
            showGuide()
            return
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish()
            toast?.cancel()
        }
    }

    fun onBackPressed(msg: String) {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis()
            showGuide(msg)
            return
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish()
            toast?.cancel()
        }
    }

    fun onBackPressed(time: Int) {
        if (System.currentTimeMillis() > backKeyPressedTime + time) {
            backKeyPressedTime = System.currentTimeMillis()
            showGuide()
            return
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + time) {
            activity.finish()
            toast?.cancel()
        }
    }

    fun onBackPressed(msg: String, time: Int) {
        if (System.currentTimeMillis() > backKeyPressedTime + time) {
            backKeyPressedTime = System.currentTimeMillis()
            showGuide(msg)
            return
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + time) {
            activity.finish()
            toast?.cancel()
        }
    }

    private fun showGuide() {
        showGuide("\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.")
    }

    private fun showGuide(msg: String) {
        toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT)
        toast?.show()
    }
}