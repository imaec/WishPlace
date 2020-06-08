package com.imaec.wishplace.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.imaec.wishplace.repository.CategoryRepository
import com.imaec.wishplace.repository.KeywordRepository
import com.imaec.wishplace.repository.PlaceRepository
import com.imaec.wishplace.viewmodel.*

class BaseViewModelFactory(private vararg val repository: Any) : ViewModelProvider.Factory {

    private val TAG = this::class.java.simpleName

    /**
     * ViewModel을 생성
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var categoryRepository: CategoryRepository? = null
        var placeRepository: PlaceRepository? = null
        var keywordRepository: KeywordRepository? = null

        if (repository.isNotEmpty()) {
            if (repository.size == 1) {
                when (val repo = repository[0]) {
                    is CategoryRepository -> categoryRepository = repo
                    is PlaceRepository -> placeRepository = repo
                    is KeywordRepository -> keywordRepository = repo
                }
            } else if (repository.size == 2) {
                when (val repo = repository[0]) {
                    is CategoryRepository -> categoryRepository = repo
                    is PlaceRepository -> placeRepository = repo
                    is KeywordRepository -> keywordRepository = repo
                }
                when (val repo = repository[1]) {
                    is CategoryRepository -> categoryRepository = repo
                    is PlaceRepository -> placeRepository = repo
                    is KeywordRepository -> keywordRepository = repo
                }
            }
        }
//        for (repo in repository.iterator()) {
//            Log.d(TAG, "    ## ${repo::class.java.simpleName}")
//            when (repo) {
//                is CategoryRepository -> categoryRepository = repo
//                is PlaceRepository -> placeRepository = repo
//                is KeywordRepository -> keywordRepository = repo
//            }
//        }
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
}