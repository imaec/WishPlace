package com.imaec.wishplace.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.imaec.wishplace.R
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.room.AppDatabase
import com.imaec.wishplace.room.dao.CategoryDao
import com.imaec.wishplace.room.entity.CategoryEntity
import com.imaec.wishplace.utils.Utils
import com.kakao.kakaolink.v2.KakaoLinkResponse
import com.kakao.kakaolink.v2.KakaoLinkService
import com.kakao.message.template.ButtonObject
import com.kakao.message.template.ContentObject
import com.kakao.message.template.FeedTemplate
import com.kakao.message.template.LinkObject
import com.kakao.network.ErrorResult
import com.kakao.network.callback.ResponseCallback
import kotlinx.coroutines.*

class SettingViewModel(context: Context) : BaseViewModel(context) {

    private val categoryDao: CategoryDao by lazy { AppDatabase.getInstance(context).categoryDao() }

    val appVersion = MutableLiveData<String>().set(Utils.getVersion(context))

    private fun getCount(category: String, callback: (Int) -> Unit) {
        viewModelScope.launch {
            var count = 0
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

    fun share(callback: (KakaoLinkResponse?) -> Unit) {
        KakaoLinkService.getInstance()
            .sendCustom(context, context.getString(R.string.template_id_app), null, object : ResponseCallback<KakaoLinkResponse>() {
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