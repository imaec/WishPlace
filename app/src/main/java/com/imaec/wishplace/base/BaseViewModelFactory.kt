package com.imaec.wishplace.base

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.imaec.wishplace.viewmodel.*

class BaseViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private val TAG = this::class.java.simpleName

    /**
     * ViewModel을 생성
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(context) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(context) as T
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> SearchViewModel(context) as T
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> SettingViewModel(context) as T
            modelClass.isAssignableFrom(WriteViewModel::class.java) -> WriteViewModel(context) as T
            else -> throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}