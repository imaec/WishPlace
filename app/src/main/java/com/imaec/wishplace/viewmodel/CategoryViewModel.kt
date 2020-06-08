package com.imaec.wishplace.viewmodel

import androidx.lifecycle.MutableLiveData
import com.imaec.wishplace.CategoryUpdateResult
import com.imaec.wishplace.TYPE_CATEGORY_EDIT
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.repository.CategoryRepository
import com.imaec.wishplace.repository.PlaceRepository
import com.imaec.wishplace.room.entity.CategoryEntity
import com.imaec.wishplace.ui.adapter.CategoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class CategoryViewModel(
    private val categoryRepository: CategoryRepository,
    private val placeRepository: PlaceRepository
) : BaseViewModel() {

    init {
        adapter = CategoryAdapter(TYPE_CATEGORY_EDIT)
    }

    val liveListCategory = MutableLiveData<ArrayList<Any>>().set(ArrayList())

    fun selectCategory() {
        viewModelScope.launch {
            categoryRepository.getList { listCategory ->
                viewModelScope.launch { liveListCategory.value = listCategory as ArrayList<Any> }
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
            placeRepository.getListByCategory(entity.categoryId) { listPlace ->
                listPlace.forEach {
                    it.category = entity.category
                }

                launch {
                    placeRepository.update(*listPlace.toTypedArray()) { result ->
                        if (result > 0) callback(CategoryUpdateResult.SUCCESS)
                        else callback(CategoryUpdateResult.FAIL)
                    }
                }
            }
        }
    }

    fun delete(entity: CategoryEntity, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            placeRepository.getListByCategory(entity.categoryId) { listPlace ->
                if (listPlace.isNotEmpty()) {
                    // 카테고리를 사용하는 장소가 있으면 false
                    launch { callback(false) }
                } else {
                    // 삭제 처리
                    launch {
                        categoryRepository.delete(entity)
                        callback(true)
                    }
                }
            }
        }
    }

    fun addOnDeleteClickListener(onClick: (CategoryEntity) -> Unit) {
        (adapter as CategoryAdapter).addOnDeleteClickListener(onClick)
    }
}