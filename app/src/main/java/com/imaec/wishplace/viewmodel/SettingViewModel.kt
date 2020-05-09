package com.imaec.wishplace.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.CategoryDao
import com.imaec.wishplace.room.entity.CategoryEntity
import com.imaec.wishplace.utils.Utils
import kotlinx.coroutines.*

class SettingViewModel(context: Context) : BaseViewModel(context) {

    private val job = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + job)
    private val categoryDao: CategoryDao by lazy { AppDatabase.getInstance(context).categoryDao() }

    val appVersion = MutableLiveData<String>().set(Utils.getVersion(context))

    fun addCategory(category: String, callback: (Boolean) -> Unit) {
        getCount(category) { count ->
            if (count == 0) {
                insert(category) {
                    callback(true)
                }
            } else {
                callback(false)
            }
        }
    }

    private fun getCount(category: String, callback: (Int) -> Unit) {
        var count = 0
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                count = categoryDao.getCount(category)
            }
            callback(count)
        }
    }

    private fun insert(category: String, callback: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                categoryDao.insert(CategoryEntity(category = category))
            }
            callback()
        }
    }
}