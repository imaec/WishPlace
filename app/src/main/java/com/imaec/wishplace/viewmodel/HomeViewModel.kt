package com.imaec.wishplace.viewmodel

import android.net.Uri
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.imaec.wishplace.R
import com.imaec.wishplace.RESULT_WRITE
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.model.NaverPlaceDTO
import com.imaec.wishplace.ui.adapter.HomeAdapter
import com.imaec.wishplace.repository.PlaceRepository
import com.imaec.wishplace.retrofit.NaverAPI
import com.imaec.wishplace.retrofit.RetrofitClient
import com.imaec.wishplace.room.entity.CategoryEntity
import com.imaec.wishplace.room.entity.PlaceEntity
import com.imaec.wishplace.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("LABEL_NAME_CLASH", "UNCHECKED_CAST")
class HomeViewModel(
    private val placeRepository: PlaceRepository
) : BaseViewModel() {

    init {
        adapter = HomeAdapter()
    }

    private val _listItem = MutableLiveData<ArrayList<Any>>(ArrayList())
    val listItem: LiveData<ArrayList<Any>>
        get() = _listItem

    private val _listRecommend = MutableLiveData<ArrayList<Any>>(ArrayList())
    val listRecommend: LiveData<ArrayList<Any>>
        get() = _listRecommend

    private val listRecommendTemp = ArrayList<PlaceEntity>()

    private fun getRecommendData() {
        // callback: (NaverPlaceResult) -> Unit
        val arrCategory = arrayOf("카페", "여행", "볼링", "고기")
        var callCount = 0
        val api = RetrofitClient.getInstnace().create(NaverAPI::class.java)
        arrCategory.forEach { category ->
            val callPlace = api.callPlace(query = category, display = 5)
            callPlace.enqueue(object : Callback<NaverPlaceDTO> {
                override fun onResponse(call: Call<NaverPlaceDTO>, response: Response<NaverPlaceDTO>) {
                    response.body()?.let {
                        Log.d(TAG, "    ## result : $it")
                        callCount++
                        transData(it.items, category, callCount, arrCategory.size)
                    }
                }

                override fun onFailure(call: Call<NaverPlaceDTO>, t: Throwable) {

                }
            })
        }
    }

    private fun transData(list: List<NaverPlaceDTO.Item>, category: String, callCount: Int, total: Int) {
        list.forEach { item ->
            val title = Html.fromHtml(item.title).toString()
            val address = item.roadAddress
            val site = item.link

            checkUrl(site, { url ->
                listRecommendTemp.add(PlaceEntity(
                    foreignId = 0,
                    name = title,
                    address = address,
                    siteUrl = site,
                    imageUrl = url
                ))

                if (listRecommendTemp.size == 5 * total) {
                    _listRecommend.value = listRecommendTemp as ArrayList<Any>
                    listRecommendTemp.clear()
                }
            }, {
                listRecommendTemp.add(PlaceEntity(
                    foreignId = 0,
                    name = title,
                    address = address,
                    siteUrl = site,
                    imageUrl = ""
                ))

                if (listRecommendTemp.size == 5 * total) {
                    _listRecommend.value = listRecommendTemp as ArrayList<Any>
                    listRecommendTemp.clear()
                }
            })
        }
    }

    private fun checkUrl(url: String, onSuccess: (String) -> Unit, onFail: (String?) -> Unit) {
        if (url.isEmpty()) {
            onFail("EMPTY_URL")
            return
        }

        getImgUrl(url) {
            if (it == null) onFail(null)
            else onSuccess(it)
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

                if (listTemp.size == 0 && listRecommendTemp.size == 0) getRecommendData()
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