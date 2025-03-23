package com.mariammuhammad.mvvmproducts.Model

 sealed class ProductResponse<out T> {
        data class Success<out T>(val data: T) : ProductResponse<T>()
        data class Error(val message: String) : ProductResponse<Nothing>()
        object Loading : ProductResponse<Nothing>()
    }
