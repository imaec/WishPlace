package com.imaec.wishplace.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.PlaceDao
import com.imaec.wishplace.room.entity.PlaceEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel(context: Context) : BaseViewModel(context) {

    private val dao: PlaceDao by lazy { AppDatabase.getInstance(context).placeDao() }

    val livePlace = MutableLiveData<PlaceEntity>()
    val liveTitle = MutableLiveData<String>()
    val liveAddress = MutableLiveData<String>()
    val liveImgUrl = MutableLiveData<String>()
    val liveSite = MutableLiveData<String>()
    val liveIsVisit = MutableLiveData<Boolean>()

    fun setData(title: String, address: String, imgUrl: String, site: String, isVisit: Boolean) {
        liveTitle.value = title
        liveAddress.value = address
        liveImgUrl.value = imgUrl
        liveSite.value = site
        liveIsVisit.value = isVisit
    }

    fun getData(placeId: Int) {
        var place: PlaceEntity? = null
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                place = dao.select(placeId)
            }
            livePlace.value = place
        }
    }

    fun delete(entity: PlaceEntity?, callback: (Boolean) -> Unit) {
        if (entity == null) {
            callback(false)
            return
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dao.delete(entity)
            }
            callback(true)
        }
    }
}