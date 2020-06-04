package com.imaec.wishplace.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.imaec.wishplace.R
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.PlaceDao
import com.imaec.wishplace.room.entity.PlaceEntity
import com.kakao.kakaolink.v2.KakaoLinkResponse
import com.kakao.kakaolink.v2.KakaoLinkService
import com.kakao.network.ErrorResult
import com.kakao.network.callback.ResponseCallback
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

    fun share(callback: (KakaoLinkResponse?) -> Unit) {
        val templateArgs: MutableMap<String, String> = HashMap()
        templateArgs["thumb"] = if (liveImgUrl.value.isNullOrEmpty()) "https://k.kakaocdn.net/14/dn/btqEEgbQEVZ/vkV9mSPTnzWLDQojPwTS5k/o.jpg" else liveImgUrl.value!!
        templateArgs["name"] = liveTitle.value ?: "Wish Place"
        templateArgs["addr"] = liveAddress.value ?: ""
        templateArgs["site"] = liveSite.value ?: ""

        KakaoLinkService.getInstance()
            .sendCustom(context, context.getString(R.string.template_id_detail), templateArgs, object : ResponseCallback<KakaoLinkResponse>() {
                override fun onSuccess(result: KakaoLinkResponse?) {
                    callback(result)
                }

                override fun onFailure(errorResult: ErrorResult?) {
                    Log.e(TAG, errorResult.toString())
                    Toast.makeText(context, "공유하기에 실패했습니다.\n잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            })
    }
}