package com.imaec.wishplace.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.imaec.wishplace.R
import com.imaec.wishplace.base.BaseViewModel
import com.imaec.wishplace.repository.CategoryRepository
import com.imaec.wishplace.room.entity.CategoryEntity
import com.imaec.wishplace.utils.Utils
import com.kakao.kakaolink.v2.KakaoLinkResponse
import com.kakao.kakaolink.v2.KakaoLinkService
import com.kakao.network.ErrorResult
import com.kakao.network.callback.ResponseCallback
import kotlinx.coroutines.*

class SettingViewModel(
    private val categoryRepository: CategoryRepository
) : BaseViewModel() {

    val appVersion = MutableLiveData<String>().set(Utils.getVersion(context))

    fun addCategory(category: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            categoryRepository.getCount(category) { count ->
                if (count == 0) {
                    launch { categoryRepository.insert(CategoryEntity(category = category)) }
                }
                callback(count == 0)
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