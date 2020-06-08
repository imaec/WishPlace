package com.imaec.wishplace.viewmodel

import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.repository.CategoryRepository
import com.imaec.wishplace.room.entity.CategoryEntity
import kotlinx.coroutines.launch

class MainViewModel(
    private val categoryRepository: CategoryRepository
) : BaseViewModel() {

    fun isExistCategory(callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            categoryRepository.getCount { count ->
                launch { callback(count > 0) }
            }
        }
    }

    fun addCategory(category: String, callback: () -> Unit) {
        viewModelScope.launch {
            categoryRepository.insert(CategoryEntity(category = category))
            callback()
        }
    }
}