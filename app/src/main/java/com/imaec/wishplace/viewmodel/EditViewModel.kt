package com.imaec.wishplace.viewmodel

import android.net.Uri
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

    val livePlace = MutableLiveData<PlaceEntity>()
    val liveTitle = MutableLiveData<String>()
    val liveAddress = MutableLiveData<String>()
    val liveImgUrl = MutableLiveData<String>()
    val liveSite = MutableLiveData<String>()
    val liveContent = MutableLiveData<String>()
    val liveIsVisit = MutableLiveData<Boolean>()

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

    fun setData(title: String, address: String, imgUrl: String, site: String, content: String, isVisit: Boolean) {
        liveTitle.value = title
        liveAddress.value = address
        liveImgUrl.value = imgUrl
        liveSite.value = site
        liveContent.value = content
        liveIsVisit.value = isVisit
    }

    fun getPlace(placeId: Int) {
        viewModelScope.launch {
            placeRepository.getPlace(placeId) { place ->
                launch { livePlace.value = place }
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