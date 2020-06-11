package com.imaec.wishplace.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.ui.adapter.HomeAdapter
import com.imaec.wishplace.repository.PlaceRepository
import com.imaec.wishplace.room.entity.CategoryEntity
import com.imaec.wishplace.room.entity.PlaceEntity
import kotlinx.coroutines.launch

@Suppress("LABEL_NAME_CLASH")
class HomeViewModel(
    private val placeRepository: PlaceRepository
) : BaseViewModel() {

    init {
        adapter = HomeAdapter()
    }

    private val _listItem = MutableLiveData<ArrayList<Any>>().set(ArrayList())
    val listItem: LiveData<ArrayList<Any>>
        get() = _listItem

    fun getData(unifiedNativeAd: UnifiedNativeAd?) {
        viewModelScope.launch {
            placeRepository.getList { listPlace ->
                var i = 0
                val listTemp = ArrayList<Any>()
                listPlace
                    .sortedByDescending { it.saveTime }
                    .groupBy { it.category }
                    .forEach all@{
                        var j = 0
                        listTemp.add(CategoryEntity(it.value[0].foreignId, it.key))
                        it.value.forEach value@{ entity ->
                            if (i == 0 && j == 2 && unifiedNativeAd != null) {
                                listTemp.add(unifiedNativeAd)
                            } else if (j < 4) {
                                listTemp.add(entity)
                            }
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