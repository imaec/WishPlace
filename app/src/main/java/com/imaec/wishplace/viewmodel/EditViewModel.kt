package com.imaec.wishplace.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.imaec.wishplace.ValidateResult
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.repository.PlaceRepository
import com.imaec.wishplace.room.entity.PlaceEntity
import com.imaec.wishplace.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class EditViewModel(
    private val placeRepository: PlaceRepository
) : BaseViewModel() {

    private val _place = MutableLiveData<PlaceEntity>()
    val place: LiveData<PlaceEntity>
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

    private fun getImgUrl(url: String, callback: (String?) -> Unit) {
        val url2 = if (Utils.isNaverBolg(url)) {
            if (!url.contains("PostView.nhn")) {
                Uri.parse(url).path?.let {
                    val blogId = it.split("/")[1]
                    val logNo = it.split("/")[2]
                    "https://blog.naver.com/PostView.nhn?blogId=$blogId&logNo=$logNo"
                } ?: run {
                    ""
                }
            } else {
                url
            }
        } else {
            url
        }
        viewModelScope.launch {
            var imgUrl: String? = null
            withContext(Dispatchers.IO) {
                try {
                    val doc = Jsoup.connect(url2).get()
                    val elements = doc.select("meta")
                    elements.forEach {
                        if (it.attr("property").equals("og:image", true)) {
                            imgUrl = it.attr("content")
                            return@withContext
                        }
                    }
                } catch (e: Exception) {
                }
            }
            callback(imgUrl)
        }
    }

    fun setData(category: String, title: String, address: String, imgUrl: String, site: String, content: String, isVisit: Boolean) {
        _category.value = category
        _title.value = title
        _address.value = address
        _imgUrl.value = imgUrl
        _site.value = site
        _content.value = content
        _isVisit.value = isVisit
    }

    fun setCategory(category: String?) {
        _category.value = category
    }

    fun setTitle(title: String?) {
        _title.value = title
    }

    fun setAddress(address: String?) {
        _address.value = address
    }

    fun setSite(site: String?) {
        _site.value = site
    }

    fun setContent(content: String?) {
        _content.value = content
    }

    fun getPlace(placeId: Int) {
        viewModelScope.launch {
            placeRepository.getPlace(placeId) { place ->
                launch { _place.value = place }
            }
        }
    }

    fun validateData(category: String, title: String, address: String) : ValidateResult {
        if (category.isEmpty()) return ValidateResult.FAIL_CATEGORY
        if (title.isEmpty()) return ValidateResult.FAIL_TITLE
        if (address.isEmpty()) return ValidateResult.FAIL_ADDRESS
        return ValidateResult.SUCCESS
    }

    fun checkUrl(url: String, onSuccess: (String) -> Unit, onFail: (String?) -> Unit) {
        if (url.isEmpty()) {
            onFail("EMPTY_URL")
            return
        }

        getImgUrl(url) {
            if (it == null) onFail(null)
            else onSuccess(it)
        }
    }

    fun update(entity: PlaceEntity, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            placeRepository.update(entity) { result ->
                launch { callback(result > 0) }
            }
        }
    }
}