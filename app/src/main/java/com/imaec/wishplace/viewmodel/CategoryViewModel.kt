package com.imaec.wishplace.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.imaec.wishplace.CategoryUpdateResult
import com.imaec.wishplace.TYPE_CATEGORY_EDIT
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.CategoryDao
import com.imaec.wishplace.room.dao.PlaceDao
import com.imaec.wishplace.room.entity.CategoryEntity
import com.imaec.wishplace.ui.adapter.CategoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
class CategoryViewModel(context: Context) : BaseViewModel(context) {

    private val categoryDao: CategoryDao by lazy { AppDatabase.getInstance(context).categoryDao() }
    private val placeDao: PlaceDao by lazy { AppDatabase.getInstance(context).placeDao() }

    var layoutManager = LinearLayoutManager(context)
    val liveListCategory = MutableLiveData<ArrayList<Any>>().set(ArrayList())

    init {
        adapter = CategoryAdapter(TYPE_CATEGORY_EDIT)
    }

    private fun selectPlaceByCategoryId(categoryId: Int, callback: (Int) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                callback(placeDao.selectByCategory(categoryId).size)
            }
        }
    }

    private fun selectCategoryByCategory(category: String, callback: (Int) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                callback(categoryDao.getCount(category))
            }
        }
    }

    fun selectCategory() {
        Log.d(TAG, "    ## select")
        viewModelScope.launch(Dispatchers.IO) {
            val listCategory = categoryDao.select() as ArrayList<Any>
            launch(Dispatchers.Main) {
                liveListCategory.value = listCategory
            }
        }
    }

    fun update(entity: CategoryEntity, category: String, callback: (CategoryUpdateResult) -> Unit) {
        selectCategoryByCategory(category) { size ->
            if (size > 0) {
                viewModelScope.launch { callback(CategoryUpdateResult.FAIL_EXIST) }
            } else {
                entity.category = category
                val resultCategory = categoryDao.update(entity)

                if (resultCategory == 0) {
                    viewModelScope.launch { callback(CategoryUpdateResult.FAIL) }
                }

                updatePlace(entity) { result -> callback(result) }
            }
        }
    }

    fun updatePlace(entity: CategoryEntity, callback: (CategoryUpdateResult) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val listPlace = placeDao.selectByCategory(entity.categoryId)
                listPlace.forEach {
                    it.category = entity.category
                }

                val result = placeDao.update(*listPlace.toTypedArray())
                viewModelScope.launch {
                    if (result > 0) callback(CategoryUpdateResult.SUCCESS)
                    else callback(CategoryUpdateResult.FAIL)
                }
            }
        }
    }

    fun delete(entity: CategoryEntity, callback: (Boolean) -> Unit) {
        selectPlaceByCategoryId(entity.categoryId) { size ->
            if (size > 0) {
                viewModelScope.launch { callback(false) }
            } else {
                // 삭제 처리
                categoryDao.delete(entity)
                viewModelScope.launch { callback(true) }
            }
        }
    }

    fun addOnDeleteClickListener(onClick: (CategoryEntity) -> Unit) {
        (adapter as CategoryAdapter).addOnDeleteClickListener(onClick)
    }
}