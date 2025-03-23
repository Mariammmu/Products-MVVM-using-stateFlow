package com.mariammuhammad.mvvmproducts.Model

import com.mariammuhammad.mvvmproducts.Model.DTO.Product
import com.mariammuhammad.mvvmproducts.Model.Remote.ProductRemoteDataSource
import com.mariammuhammad.mvvmproducts.Model.local.ProductLocalDataSource
import kotlinx.coroutines.flow.Flow

class ProductRepository
   (private val remoteDataSource: ProductRemoteDataSource,
    private val localDataSource: ProductLocalDataSource){

    suspend fun getAllProducts(isRemote:Boolean): Flow<List<Product>>? {
        if(isRemote)
            return remoteDataSource.getAllProducts()

        else
            return localDataSource.getAllProducts()
    }

    suspend fun insertProduct(product: Product):Long{
        return localDataSource.insertProduct(product)
    }

    suspend fun deleteProduct(product: Product):Int{
        return localDataSource.deleteProduct(product)
    }
}