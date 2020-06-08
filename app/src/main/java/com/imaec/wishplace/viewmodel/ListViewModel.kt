package com.imaec.wishplace.viewmodel

import androidx.lifecycle.MutableLiveData
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.repository.PlaceRepository
import com.imaec.wishplace.room.entity.PlaceEntity
import com.imaec.wishplace.ui.adapter.ListAdapter
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class ListViewModel(
    private val placeRepository: PlaceRepository
) : BaseViewModel() {

    val liveCategory = MutableLiveData<String>()
    val liveListItem = MutableLiveData<ArrayList<Any>>().set(ArrayList())
    var isUpdated = false

    init {
        adapter = ListAdapter()
    }

    fun getData(categoryId: Int) {
        viewModelScope.launch {
            val listTemp = ArrayList<Any>()
            placeRepository.getListByCategory(categoryId) { listPlace ->
                listPlace
                    .sortedByDescending { it.saveTime }
                    .groupBy { it.category }
                    .forEach {
                        it.value.forEach { entity ->
                            listTemp.add(entity)
                        }
                    }

                viewModelScope.launch {
                    liveListItem.value = listTemp
                }
            }
        }
    }

    fun delete(entity: PlaceEntity, callback: () -> Unit) {
        viewModelScope.launch {
            placeRepository.delete(entity)
            callback()
        }
    }
}