package com.imaec.wishplace.viewmodel

import androidx.lifecycle.MutableLiveData
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.repository.PlaceRepository
import com.imaec.wishplace.room.entity.PlaceEntity
import kotlinx.coroutines.launch

class DetailViewModel(
    private val placeRepository: PlaceRepository
) : BaseViewModel() {

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
    }

    fun getData(placeId: Int) {
        viewModelScope.launch {
            placeRepository.getPlace(placeId) { place ->
                launch {
                    livePlace.value = place
                    setData(place.name, place.address, place.content, place.imageUrl, place.siteUrl, place.visitFlag)
                }
            }
        }
    }

    fun delete(entity: PlaceEntity?, callback: (Boolean) -> Unit) {
        if (entity == null) {
            callback(false)
            return
        }
        viewModelScope.launch {
            placeRepository.delete(entity)
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
            placeRepository.update(entity) { result ->
                launch {
                    liveIsVisit.value = !isVisit
                    callback(result > 0)
                }
            }
        }
    }

    fun getArgs() : HashMap<String, String> {
        val templateArgs: MutableMap<String, String> = HashMap<String, String>().apply {
            this["thumb"] = if (liveImgUrl.value.isNullOrEmpty()) "https://k.kakaocdn.net/14/dn/btqEEgbQEVZ/vkV9mSPTnzWLDQojPwTS5k/o.jpg" else liveImgUrl.value!!
            this["name"] = liveTitle.value ?: "Wish Place"
            this["addr"] = liveAddress.value ?: ""
            this["site"] = liveSite.value ?: ""
        }

        return templateArgs as HashMap<String, String>
    }
}