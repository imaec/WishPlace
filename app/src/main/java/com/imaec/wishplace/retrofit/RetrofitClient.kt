package com.imaec.wishplace.retrofit

import com.imaec.wishplace.URL_NAVER_API
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private var instance : Retrofit? = null

    fun getInstnace() : Retrofit {
        if(instance == null){
            instance = Retrofit.Builder()
                .baseUrl(URL_NAVER_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return instance!!
    }
}