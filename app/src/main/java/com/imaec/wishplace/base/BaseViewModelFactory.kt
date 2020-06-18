package com.imaec.wishplace.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.imaec.wishplace.repository.CategoryRepository
import com.imaec.wishplace.repository.KeywordRepository
import com.imaec.wishplace.repository.PlaceRepository
import com.imaec.wishplace.viewmodel.*

class BaseViewModelFactory(private vararg val repository: Any) : ViewModelProvider.Factory {

    private var categoryRepository: CategoryRepository? = null
    private var placeRepository: PlaceRepository? = null
    private var keywordRepository: KeywordRepository? = null

    /**
     * ViewModel을 생성
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (repository.isNotEmpty()) {
            initRepo(repository[0])
            if (repository.size == 2) {
                initRepo(repository[1])
            }
        }
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(categoryRepository!!) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(placeRepository!!) as T
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> SearchViewModel(keywordRepository!!) as T
            modelClass.isAssignableFrom(SearchResultViewModel::class.java) -> SearchResultViewModel(placeRepository!!) as T
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> SettingViewModel(categoryRepository!!) as T
            modelClass.isAssignableFrom(LicenseViewModel::class.java) -> LicenseViewModel() as T
            modelClass.isAssignableFrom(CategoryViewModel::class.java) -> CategoryViewModel(categoryRepository!!, placeRepository!!) as T
            modelClass.isAssignableFrom(WriteViewModel::class.java) -> WriteViewModel(categoryRepository!!, placeRepository!!) as T
            modelClass.isAssignableFrom(CategorySelectViewModel::class.java) -> CategorySelectViewModel(categoryRepository!!) as T
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> DetailViewModel(placeRepository!!) as T
            modelClass.isAssignableFrom(ImageViewModel::class.java) -> ImageViewModel() as T
            modelClass.isAssignableFrom(EditViewModel::class.java) -> EditViewModel(placeRepository!!) as T
            modelClass.isAssignableFrom(ListViewModel::class.java) -> ListViewModel(placeRepository!!) as T
            else -> throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

    private fun initRepo(repo: Any) {
        when (repo) {
            is CategoryRepository -> categoryRepository = repo
            is PlaceRepository -> placeRepository = repo
            is KeywordRepository -> keywordRepository = repo
        }
    }
}