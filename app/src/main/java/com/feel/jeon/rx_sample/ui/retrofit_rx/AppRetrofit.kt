package com.feel.jeon.rx_sample.ui.retrofit_rx

import com.feel.jeon.rx_sample.ui.retrofit_rx.res.ApiResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object AppRetrofit {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.upbit.com")
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val upbitApi = retrofit.create(RetrofitService::class.java)
}

interface RetrofitService {
    /**
     * 값을 받아오는 GET의 경우 보통 Single을 이용하고, POST,DELETE,PUT의 경우 Completable을 이용합니다! (반드시는 아니지만 보통의 경우 그렇습니다!)
     */
    @GET("v1/market/all")
    fun getMarketCode() : Single<ApiResponse>
}