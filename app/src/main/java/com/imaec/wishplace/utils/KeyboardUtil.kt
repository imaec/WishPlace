package com.imaec.wishplace.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager

class KeyboardUtil {

    companion object {
        fun hideKeyboardFrom(context: Context) {
            try {
                val imm = (context as Activity).getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                context.currentFocus?.let {
                    imm.hideSoftInputFromWindow(it.windowToken, 0)
                }
            } catch (e: Exception) {
                Log.d("exception :::: ", e.toString())
            }
        }

        fun showKeyboard(context: Context) {
            try {
                val imm = (context as Activity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                context.currentFocus?.let {
                    imm.toggleSoftInputFromWindow(it.windowToken, InputMethodManager.SHOW_FORCED, 0)
                }
            } catch (e: Exception) {
                Log.d("exception :::: ", e.toString())
            }
        }
    }
}