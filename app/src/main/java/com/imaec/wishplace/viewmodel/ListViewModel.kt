package com.imaec.wishplace.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.repository.PlaceRepository
import com.imaec.wishplace.room.entity.PlaceEntity
import com.imaec.wishplace.ui.adapter.ListAdapter
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class ListViewModel(
    private val placeRepository: PlaceRepository
) : BaseViewModel() {

    private val _category = MutableLiveData<String>()
    val category: LiveData<String>
        get() = _category
    private val _listItem = MutableLiveData<ArrayList<Any>>(ArrayList())
    val listItem: LiveData<ArrayList<Any>>
        get() = _listItem

    init {
        adapter = ListAdapter()
    }

    fun setCategory(category: String) {
        _category.value = category
    }

    fun getData(categoryId: Int, unifiedNativeAd: UnifiedNativeAd?) {
        var i = 0
        var j = 0
        viewModelScope.launch {
            placeRepository.getListByCategory(categoryId) { listPlace ->
                val listTemp = ArrayList<Any>()
                listPlace
                    .sortedByDescending { it.saveTime }
                    .groupBy { it.category }
                    .forEach {
                        it.value.forEach { entity ->
                            if (i == 0 && j == 2 && unifiedNativeAd != null) {
                                listTemp.add(unifiedNativeAd)
                            }
                            listTemp.add(entity)
                            j++
                        }
                        i++
                    }

                launch { _listItem.value = listTemp }
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