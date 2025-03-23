package com.mariammuhammad.mvvmproducts.Model.Remote

import com.mariammuhammad.mvvmproducts.Model.DTO.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProductRemoteDataSource(private val service: ApiCall) {

//    suspend fun getAllProducts(): List<Product>?{
//        return service.getAllProducts().body()?.products
//    }

        suspend fun getAllProducts(): Flow<List<Product>>? {
            return flow {
                val response = service.getAllProducts()

                if (response.isSuccessful) {
                    emit(response.body()?.products ?: emptyList())
                } else {
                    emit(emptyList())
                }
            }.flowOn(Dispatchers.IO)
        }

}