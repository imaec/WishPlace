package com.imaec.wishplace.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.repository.CategoryRepository
import com.imaec.wishplace.room.entity.CategoryEntity
import kotlinx.coroutines.launch

class SettingViewModel(
    private val categoryRepository: CategoryRepository
) : BaseViewModel() {

    private val _appVersion = MutableLiveData<String>("")
    val appVersion: LiveData<String>
        get() = _appVersion

    fun setAppVersion(appVersion: String) {
        _appVersion.value = appVersion
    }

    fun addCategory(category: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            categoryRepository.getCount(category) { count ->
                if (count == 0) {
                    launch { categoryRepository.insert(CategoryEntity(category = category)) }
                }
                launch { callback(count == 0) }
            }
        }
    }
}