package com.imaec.wishplace.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.imaec.wishplace.*
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.CategoryDao
import com.imaec.wishplace.room.dao.PlaceDao
import com.imaec.wishplace.room.entity.CategoryEntity
import com.imaec.wishplace.room.entity.PlaceEntity
import com.imaec.wishplace.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class WriteViewModel(context: Context) : BaseViewModel(context) {

    private val categoryDao: CategoryDao by lazy { AppDatabase.getInstance(context).categoryDao() }
    private val placeDao: PlaceDao by lazy { AppDatabase.getInstance(context).placeDao() }

    val liveCategory = MutableLiveData<String>().set("")

    fun addCategory(category: String, callback: (Boolean) -> Unit) {
        getCount(category) { count ->
            if (count == 0) {
                insert(category) {
                    callback(true)
                }
            } else {
                callback(false)
            }
        }
    }

    fun validateData(category: String, title: String, address: String) : WriteResult {
        if (category.isEmpty()) return WriteResult.FAIL_CATEGORY
        if (title.isEmpty()) return WriteResult.FAIL_TITLE
        if (address.isEmpty()) return WriteResult.FAIL_ADDRESS
        return WriteResult.SUCCESS
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

    fun save(entity: PlaceEntity, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                placeDao.insert(entity)
            }
            callback(true)
        }
    }

    private fun getCount(category: String, callback: (Int) -> Unit) {
        var count = 0
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                count = categoryDao.getCount(category)
            }
            callback(count)
        }
    }

    private fun insert(category: String, callback: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                categoryDao.insert(CategoryEntity(category = category))
            }
            callback()
        }
    }

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
}