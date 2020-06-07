package com.imaec.wishplace.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.imaec.wishplace.*
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.model.NaverPlaceDTO
import com.imaec.wishplace.repository.CategoryRepository
import com.imaec.wishplace.retrofit.NaverAPI
import com.imaec.wishplace.retrofit.RetrofitClient
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.CategoryDao
import com.imaec.wishplace.room.dao.PlaceDao
import com.imaec.wishplace.room.entity.CategoryEntity
import com.imaec.wishplace.room.entity.PlaceEntity
import com.imaec.wishplace.ui.adapter.NaverPlaceAdapter
import com.imaec.wishplace.ui.util.NaverPlaceItemDecoration
import com.imaec.wishplace.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("UNCHECKED_CAST")
class WriteViewModel(
    private val categoryRepository: CategoryRepository
) : BaseViewModel() {

    init {
        adapter = NaverPlaceAdapter()
    }

    private val placeDao: PlaceDao by lazy { AppDatabase.getInstance(context).placeDao() }

    val liveCategory = MutableLiveData<String>().set("")
    val liveNaverPlace = MutableLiveData<ArrayList<Any>>().set(ArrayList())
    val layoutManager = LinearLayoutManager(context)
    val itemDecoration = NaverPlaceItemDecoration(context)

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

    fun getNaverPlace(name: String, callback: (NaverPlaceResult) -> Unit) {
        if (name.isEmpty()) {
            callback(NaverPlaceResult.FAIL_EMPTY_NAME)
            return
        }

        val api = RetrofitClient.getInstnace().create(NaverAPI::class.java)
        val callPlace = api.callPlace(query = name)
        callPlace.enqueue(object : Callback<NaverPlaceDTO> {
            override fun onResponse(call: Call<NaverPlaceDTO>, response: Response<NaverPlaceDTO>) {
                response.body()?.let {
                    if (it.items.isEmpty()) callback(NaverPlaceResult.FAIL_EMPTY_RESULT)
                    else {
                        liveNaverPlace.value = it.items as ArrayList<Any>
                        callback(NaverPlaceResult.SUCCESS)
                    }
                }
            }

            override fun onFailure(call: Call<NaverPlaceDTO>, t: Throwable) {
                callback(NaverPlaceResult.FAIL_UNKNWON)
            }
        })
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