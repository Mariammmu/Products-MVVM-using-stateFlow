package com.mariammuhammad.mvvmproducts.Model.Remote

import com.mariammuhammad.mvvmproducts.Model.DTO.ProductRoot
import retrofit2.Response
import retrofit2.http.GET

interface ApiCall {
    @GET("products")
    //    suspend fun getAllProducts(): Response<ProductRoot>
    suspend fun getAllProducts(): Response<ProductRoot>

}