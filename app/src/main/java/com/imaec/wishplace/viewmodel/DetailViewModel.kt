package com.imaec.wishplace.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.model.PlaceDTO
import com.imaec.wishplace.repository.PlaceRepository
import com.imaec.wishplace.room.entity.PlaceEntity
import com.imaec.wishplace.utils.ModelUtil
import kotlinx.coroutines.launch

class DetailViewModel(
    private val placeRepository: PlaceRepository
) : BaseViewModel() {

    private val _place = MutableLiveData<PlaceDTO>()
    val place: LiveData<PlaceDTO>
        get() = _place
    private val _category = MutableLiveData<String>()
    val category: LiveData<String>
        get() = _category
    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title
    private val _address = MutableLiveData<String>()
    val address: LiveData<String>
        get() = _address
    private val _content = MutableLiveData<String>()
    val content: LiveData<String>
        get() = _content
    private val _imgUrl = MutableLiveData<String>()
    val imgUrl: LiveData<String>
        get() = _imgUrl
    private val _site = MutableLiveData<String>()
    val site: LiveData<String>
        get() = _site
    private val _isVisit = MutableLiveData<Boolean>()
    val isVisit: LiveData<Boolean>
        get() = _isVisit

    fun setData(category: String, title: String, address: String, content: String, imgUrl: String, site: String, isVisit: Boolean) {
        _category.value = category
        _title.value = title
        _address.value = address
        _content.value = content
        _imgUrl.value = imgUrl
        _site.value = site
        _isVisit.value = isVisit
    }

    fun getData(placeId: Int) {
        viewModelScope.launch {
            placeRepository.getPlace(placeId) { place ->
                launch {
                    _place.value = place
                }
            }
        }
    }

    fun delete(entity: PlaceEntity?, callback: (Boolean) -> Unit) {
        if (entity == null || entity.placeId == 0) {
            callback(false)
            return
        }
        viewModelScope.launch {
            placeRepository.delete(entity)
            callback(true)
        }
    }

    fun updateVisit(callback: (Boolean) -> Unit) {
        val dto = _place.value
        val isVisit = _isVisit.value

        if (isVisit == null || dto == null) {
            callback(false)
            return
        }
        dto.visitFlag = !isVisit
        viewModelScope.launch {
            placeRepository.update(ModelUtil.toPlaceEntity(dto)) { result ->
                launch {
                    _isVisit.value = !isVisit
                    callback(result > 0)
                }
            }
        }
    }

    fun getArgs() : HashMap<String, String> {
        val templateArgs: MutableMap<String, String> = HashMap<String, String>().apply {
            this["thumb"] = if (_imgUrl.value.isNullOrEmpty()) "https://k.kakaocdn.net/14/dn/btqEEgbQEVZ/vkV9mSPTnzWLDQojPwTS5k/o.jpg" else _imgUrl.value!!
            this["name"] = _title.value ?: "Wish Place"
            this["addr"] = _address.value ?: ""
            this["site"] = _site.value ?: ""
        }

        return templateArgs as HashMap<String, String>
    }
}