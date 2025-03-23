package com.mariammuhammad.mvvmproducts.Model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mariammuhammad.mvvmproducts.Model.DTO.Product

@Database(entities = [Product::class], version = 1)
abstract class ProductRoomDatabase :RoomDatabase(){
    abstract fun productDao(): ProductDao
    companion object {
        @Volatile
        private var INSTANCE: ProductRoomDatabase? = null

        fun getInstance(context: Context): ProductRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductRoomDatabase::class.java,
                    "product_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}