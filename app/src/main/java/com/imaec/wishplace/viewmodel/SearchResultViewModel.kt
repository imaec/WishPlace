package com.imaec.wishplace.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.repository.PlaceRepository
import com.imaec.wishplace.room.entity.PlaceEntity
import com.imaec.wishplace.ui.adapter.SearchAdapter
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class SearchResultViewModel(
    private val placeRepository: PlaceRepository
) : BaseViewModel() {

    init {
        adapter = SearchAdapter()
    }

    private val _listSearchItem = MutableLiveData<ArrayList<Any>>(ArrayList())
    val listSearchItem: LiveData<ArrayList<Any>>
        get() = _listSearchItem

    fun search(keyword: String, option: String, callback: (Boolean) -> Unit) {
        (adapter as SearchAdapter).setKeyword(keyword)
        (adapter as SearchAdapter).setOption(option)

        viewModelScope.launch {
            when (option) {
                "이름" -> {
                    placeRepository.getListByName("%$keyword%") { listPlace ->
                        launch { _listSearchItem.value = listPlace as ArrayList<Any> }
                    }
                }
                "주소" -> {
                    placeRepository.getListByAddress("%$keyword%") { listPlace ->
                        launch { _listSearchItem.value = listPlace as ArrayList<Any> }
                    }
                }
                else -> {
                    callback(false)
                    return@launch
                }
            }
            callback(true)
        }
    }

    fun delete(entity: PlaceEntity, callback: () -> Unit) {
        viewModelScope.launch {
            placeRepository.delete(entity)
            callback()
        }
    }
}