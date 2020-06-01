package com.imaec.wishplace.retrofit

import com.imaec.wishplace.NAVER_CLIENT_ID
import com.imaec.wishplace.NAVER_CLIENT_SECRET
import com.imaec.wishplace.model.NaverPlaceDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverAPI {

    @GET("v1/search/local.json")
    fun callPlace(
        @Header("X-Naver-Client-Id") clientId: String = NAVER_CLIENT_ID,
        @Header("X-Naver-Client-Secret") clientSecret: String = NAVER_CLIENT_SECRET,
        @Query("query") query: String,
        @Query("start") start: Int = 1,
        @Query("display") display: Int = 30,
        @Query("sort") sort: String = "random"
    ): Call<NaverPlaceDTO>
}