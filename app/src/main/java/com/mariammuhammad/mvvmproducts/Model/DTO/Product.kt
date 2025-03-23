package com.mariammuhammad.mvvmproducts.Model.DTO

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    val id: Long,
    val thumbnail: String ,
    val title: String ,
    val description: String ,
    val category: String,
    val price: Double
    )