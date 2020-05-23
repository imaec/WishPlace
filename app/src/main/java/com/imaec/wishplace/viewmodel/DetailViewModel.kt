package com.imaec.wishplace.viewmodel

import android.content.Context
import android.util.Log
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
    val liveContent = MutableLiveData<String>()
    val liveImgUrl = MutableLiveData<String>()
    val liveSite = MutableLiveData<String>()
    val liveIsVisit = MutableLiveData<Boolean>()
    var isUpdated = false

    fun setData(title: String, address: String, content: String, imgUrl: String, site: String, isVisit: Boolean) {
        liveTitle.value = title
        liveAddress.value = address
        liveContent.value = content
        liveImgUrl.value = imgUrl
        liveSite.value = site
        liveIsVisit.value = isVisit

        Log.d(TAG, "    ## $isVisit")
    }

    fun getData(placeId: Int) {
        var place: PlaceEntity? = null
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                place = dao.select(placeId)
            }
            livePlace.value = place
            place?.let {
                setData(it.name, it.address, it.content, it.imageUrl, it.siteUrl, it.visitFlag)
            }
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

    fun updateVisit(callback: (Boolean) -> Unit) {
        val entity = livePlace.value
        val isVisit = liveIsVisit.value

        if (isVisit == null || entity == null) {
            callback(false)
            return
        }
        entity.visitFlag = !isVisit
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = dao.update(entity)
                launch(Dispatchers.Main) {
                    liveIsVisit.value = !isVisit
                    callback(result > 0)
                }
            }
        }
    }
}