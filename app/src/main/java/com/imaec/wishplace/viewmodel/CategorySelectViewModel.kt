package com.imaec.wishplace.viewmodel

import androidx.lifecycle.MutableLiveData
import com.imaec.wishplace.TYPE_CATEGORY_SELECT
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.repository.CategoryRepository
import com.imaec.wishplace.ui.adapter.CategoryAdapter
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class CategorySelectViewModel(
    private val categoryRepository: CategoryRepository
) : BaseViewModel() {

    val liveListCategory = MutableLiveData<ArrayList<Any>>().set(ArrayList())

    init {
        adapter = CategoryAdapter(TYPE_CATEGORY_SELECT)
    }

    fun selectCategory() {
        viewModelScope.launch {
            categoryRepository.getList { listCategory ->
                viewModelScope.launch { liveListCategory.value = listCategory as ArrayList<Any> }
            }
        }
    }
}