package com.imaec.wishplace.viewmodel

import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.repository.CategoryRepository
import com.imaec.wishplace.room.entity.CategoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val categoryRepository: CategoryRepository
) : BaseViewModel() {

    fun isExistCategory(callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            categoryRepository.getCount { count ->
                launch(Dispatchers.Main) { callback(count > 0) }
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