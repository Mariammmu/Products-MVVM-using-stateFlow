package com.mariammuhammad.mvvmproducts.Model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mariammuhammad.mvvmproducts.Model.DTO.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    //suspend fun getAllProducts(): List<Product>
     fun getAllProducts(): Flow<List<Product>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    //suspend fun insertProduct(products: Product): Long
    //Long because we might insert many items
     fun insertProduct (products: Product): Long

    @Delete
     fun deleteProduct(products: Product): Int
    //int as it's not a heavy operation so int
    //if it was 0 or less so there's a problem, if it's more so it works fine
}