package com.imaec.wishplace.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import java.text.SimpleDateFormat
import java.util.*

class Utils {

    companion object {
        fun dp(context: Context, dpValue: Int): Int {
            val d = context.resources.displayMetrics.density
            return (dpValue * d).toInt()
        }

        fun getVersion(context: Context) : String {
            var version = "Unknown"
            val packageInfo: PackageInfo

            try {
                packageInfo = context.applicationContext
                    .packageManager
                    .getPackageInfo(context.applicationContext.packageName, 0)
                version = packageInfo.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                return version
            }
            return version
        }

        @SuppressLint("SimpleDateFormat")
        fun getDate(format: String = "yyyyMMdd") : String {
            return SimpleDateFormat(format).format(Date())
        }

        @SuppressLint("SimpleDateFormat")
        fun getDateChangeFormat(date: String, fromFormat: String = "yyyyMMdd", toFormat: String = "yyyyMMdd") : String {
            val fromSdf = SimpleDateFormat(fromFormat)
            val toSdf = SimpleDateFormat(toFormat)
            return toSdf.format(fromSdf.parse(date) ?: Date())
        }

        fun isNaverBolg(url: String) : Boolean {
            return url.contains("blog.naver.com")
        }
    }
}