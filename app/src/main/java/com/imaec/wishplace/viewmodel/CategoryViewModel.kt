package com.imaec.wishplace.viewmodel

import androidx.lifecycle.MutableLiveData
import com.imaec.wishplace.CategoryUpdateResult
import com.imaec.wishplace.TYPE_CATEGORY_EDIT
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.repository.CategoryRepository
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.PlaceDao
import com.imaec.wishplace.room.entity.CategoryEntity
import com.imaec.wishplace.ui.adapter.CategoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
class CategoryViewModel(
    private val categoryRepository: CategoryRepository
) : BaseViewModel() {

    init {
        adapter = CategoryAdapter(TYPE_CATEGORY_EDIT)
    }

    private val placeDao: PlaceDao by lazy { AppDatabase.getInstance(context).placeDao() }

    val liveListCategory = MutableLiveData<ArrayList<Any>>().set(ArrayList())

    private fun selectPlaceByCategoryId(categoryId: Int, callback: (Int) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                callback(placeDao.selectByCategory(categoryId).size)
            }
        }
    }

    private fun selectCategoryByCategory(category: String, callback: (Int) -> Unit) {
        viewModelScope.launch {
            categoryRepository.getCount(category, callback)
        }
    }

    fun selectCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.getList { listCategory ->
                liveListCategory.value = listCategory as ArrayList<Any>
            }
        }
    }

    fun update(entity: CategoryEntity, category: String, callback: (CategoryUpdateResult) -> Unit) {
        viewModelScope.launch {
            categoryRepository.getCount(category) { size ->
                if (size > 0) {
                    launch { callback(CategoryUpdateResult.FAIL_EXIST) }
                } else {
                    entity.category = category
                    launch(Dispatchers.IO) {
                        val resultCategory = categoryRepository.update(entity)
                        if (resultCategory == 0) {
                            viewModelScope.launch { callback(CategoryUpdateResult.FAIL) }
                        }

                        updatePlace(entity) { result -> callback(result) }
                    }
                }
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
                viewModelScope.launch {
                    categoryRepository.delete(entity)
                    callback(true)
                }
            }
        }
    }

    fun addOnDeleteClickListener(onClick: (CategoryEntity) -> Unit) {
        (adapter as CategoryAdapter).addOnDeleteClickListener(onClick)
    }
}