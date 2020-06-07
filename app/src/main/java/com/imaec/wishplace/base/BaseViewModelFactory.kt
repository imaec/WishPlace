package com.imaec.wishplace.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.imaec.wishplace.repository.CategoryRepository
import com.imaec.wishplace.viewmodel.*

class BaseViewModelFactory(private vararg val repository: Any) : ViewModelProvider.Factory {

    private val TAG = this::class.java.simpleName

    /**
     * ViewModel을 생성
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val categoryRepository = if (repository.isNotEmpty()) repository[0] as CategoryRepository else null
        // val placeRepository = if (repository.size > 1) repository[1] as PlaceRepository else null
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(categoryRepository!!) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel() as T
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> SearchViewModel() as T
            modelClass.isAssignableFrom(SearchResultViewModel::class.java) -> SearchResultViewModel() as T
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> SettingViewModel(categoryRepository!!) as T
            modelClass.isAssignableFrom(LicenseViewModel::class.java) -> LicenseViewModel() as T
            modelClass.isAssignableFrom(CategoryViewModel::class.java) -> CategoryViewModel(categoryRepository!!) as T
            modelClass.isAssignableFrom(WriteViewModel::class.java) -> WriteViewModel(categoryRepository!!) as T
            modelClass.isAssignableFrom(CategorySelectViewModel::class.java) -> CategorySelectViewModel() as T
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> DetailViewModel() as T
            modelClass.isAssignableFrom(ImageViewModel::class.java) -> ImageViewModel() as T
            modelClass.isAssignableFrom(EditViewModel::class.java) -> EditViewModel() as T
            modelClass.isAssignableFrom(ListViewModel::class.java) -> ListViewModel() as T
            else -> throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}