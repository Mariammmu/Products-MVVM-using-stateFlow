package com.mariammuhammad.mvvmproducts.Model.Remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private val BASE_URL ="https://dummyjson.com/"

    val getProductInstance=
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()

    val service: ApiCall= getProductInstance.create(ApiCall::class.java)
}

