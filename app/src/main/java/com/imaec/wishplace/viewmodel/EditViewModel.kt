package com.imaec.wishplace.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.PlaceDao
import com.imaec.wishplace.room.entity.PlaceEntity
import com.imaec.wishplace.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class EditViewModel(context: Context) : BaseViewModel(context) {

    private val dao: PlaceDao by lazy { AppDatabase.getInstance(context).placeDao() }

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

    fun getData(placeId: Int) {
        var place: PlaceEntity? = null
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                place = dao.select(placeId)
            }
            livePlace.value = place
        }
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

    fun update(entity: PlaceEntity?, callback: (Boolean) -> Unit) {
        if (entity == null) {
            callback(false)
            return
        }
        var result = 0
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                result = dao.update(entity)
            }
            callback(result > 0)
        }
    }
}