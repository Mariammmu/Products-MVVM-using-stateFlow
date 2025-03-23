package com.mariammuhammad.mvvmproducts.Model.local

import com.mariammuhammad.mvvmproducts.Model.DTO.Product
import kotlinx.coroutines.flow.Flow

class ProductLocalDataSource(private val productDao: ProductDao) {
//when connecting to external datasource; suspend and flow are paradox we can't use both of them
    //flow doesn't require calling the function to get updated
     suspend fun getAllProducts(): Flow<List<Product>>{
       return productDao.getAllProducts()
    }

     suspend fun insertProduct(product: Product): Long{
        return productDao.insertProduct(product)
    }

     suspend fun deleteProduct(product: Product): Int{

         if(product!=null)
             return productDao.deleteProduct(product)
        else
            return -1 //which means failed process
    }
}