package com.imaec.wishplace.utils

import android.content.Context

class SharedPreferenceManager {

    enum class KEY {
        PREF_SEARCH_OPTION,
    }

    companion object {

        private const val NAME = "WishPlace"

        fun getString(context: Context, key: KEY, def: String = "") : String {
            val pref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
            return pref.getString(key.name, def) ?: ""
        }

        fun getInt(context: Context, key: KEY, def: Int = 0) : Int {
            val pref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
            return pref.getInt(key.name, def)
        }

        fun getBool(context: Context, key: KEY, def: Boolean = false) : Boolean {
            val pref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
            return pref.getBoolean(key.name, def)
        }

        fun putValue(context: Context, key: KEY, value: String) {
            val pref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
            pref.edit().apply {
                putString(key.name, value)
                apply()
            }
        }

        fun putValue(context: Context, key: KEY, value: Int) {
            val pref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
            pref.edit().apply {
                putInt(key.name, value)
                apply()
            }
        }

        fun putValue(context: Context, key: KEY, value: Boolean) {
            val pref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
            pref.edit().apply {
                putBoolean(key.name, value)
                apply()
            }
        }
    }
}