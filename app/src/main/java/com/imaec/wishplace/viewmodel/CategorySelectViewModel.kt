package com.imaec.wishplace.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.CategoryDao
import com.imaec.wishplace.ui.adapter.CategoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
class CategorySelectViewModel(context: Context) : BaseViewModel(context) {

    private val categoryDao: CategoryDao by lazy { AppDatabase.getInstance(context).categoryDao() }

    var layoutManager = LinearLayoutManager(context)
    val liveListCategory = MutableLiveData<ArrayList<Any>>().set(ArrayList())

    init {
        adapter = CategoryAdapter()
    }

    fun selectCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            val listCategory = categoryDao.select() as ArrayList<Any>
            launch(Dispatchers.Main) {
                liveListCategory.value = listCategory
            }
        }
    }
}