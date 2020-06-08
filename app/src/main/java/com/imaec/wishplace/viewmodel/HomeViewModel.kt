package com.imaec.wishplace.viewmodel

import androidx.lifecycle.MutableLiveData
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.ui.adapter.HomeAdapter
import com.imaec.wishplace.repository.PlaceRepository
import com.imaec.wishplace.room.entity.CategoryEntity
import com.imaec.wishplace.room.entity.PlaceEntity
import kotlinx.coroutines.launch

class HomeViewModel(
    private val placeRepository: PlaceRepository
) : BaseViewModel() {

    init {
        adapter = HomeAdapter()
    }

    val liveListItem = MutableLiveData<ArrayList<Any>>().set(ArrayList())

    fun getData() {
        viewModelScope.launch {
            placeRepository.getList { listPlace ->
                val listTemp = ArrayList<Any>()
                listPlace
                    .sortedByDescending { it.saveTime }
                    .groupBy { it.category }
                    .forEach {
                        listTemp.add(CategoryEntity(it.value[0].foreignId, it.key))
                        it.value.forEach { entity ->
                            listTemp.add(entity)
                        }
                    }

                launch {
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