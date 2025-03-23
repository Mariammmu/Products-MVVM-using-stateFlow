package com.mariammuhammad.mvvmproducts.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mariammuhammad.mvvmproducts.Model.DTO.Product
import com.mariammuhammad.mvvmproducts.Model.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response

class ProductViewModel(private val repo: ProductRepository) : ViewModel() {
    private val mutableProducts = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = mutableProducts

    private val mutableMessage = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = mutableMessage

    private val mutableFavoriteProducts = MutableStateFlow<List<Product>?>(null)
    val favoriteProducts: StateFlow<List<Product>?> = mutableFavoriteProducts

    // Fetch all products
    fun getProducts() {
        viewModelScope.launch {
            repo.getAllProducts(true)
                ?.catch { ex ->
                    mutableMessage.value = "Error from API: ${ex.message}"
                    mutableProducts.value = emptyList()
                }
                ?.collect { result ->
                    if (result.isNotEmpty()) {
                        mutableProducts.value = result
                    } else {
                        mutableMessage.value = "No products found."
                    }
                }
        }
    }

    fun addToFavorite(product: Product) {
        if (product != null) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val result = repo.insertProduct(product)
                    if (result > 0) {
                        mutableMessage.value = "Product Added Successfully"
                    } else {
                        mutableMessage.value = "Product is already in favorite!"
                    }
                } catch (e: Exception) {
                    mutableMessage.value = "Couldn't add product, ${e.message}"
                }
            }
        } else {
            mutableMessage.value = "Couldn't Add Product, Missing product"
        }
    }

    fun getFavoriteProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAllProducts(false)
                ?.collect {
                    mutableFavoriteProducts.value = it
                }
        }
    }

    // Delete a product from favorites
    fun deleteFavorite(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repo.deleteProduct(product)
                if (result > 0) {
                    mutableMessage.value = "Product Removed Successfully"
                   // getFavoriteProducts()
                }
            } catch (e: Exception) {
                mutableMessage.value = "Couldn't remove product, ${e.message}"
            }
        }
    }
}

class AllProductsFactory(private val _repo: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(_repo) as T
        } else {
            throw IllegalArgumentException("ViewModel class not found")
        }
    }
}

/*
class ProductViewModel(private val repo: ProductRepository): ViewModel() {
    private val mutableProduct: MutableLiveData<List<Product>> = MutableLiveData() //observable
    val products : LiveData<List<Product>> = mutableProduct

    private val mutableMessage: MutableLiveData<String> = MutableLiveData()
    val message : LiveData<String> = mutableMessage
//to involve the user with me and make him able to see what's going on on the UI

    private val mutableFavoriteProducts: MutableLiveData<List<Product>?> = MutableLiveData()
    val favoriteProducts: MutableLiveData<List<Product>?> = mutableFavoriteProducts

    fun getProducts(){
        viewModelScope.launch (Dispatchers.IO){
            //we do try&catch in case any exception occurred
            val result= repo.getAllProducts(true)
            if(result!=null) {
                val list: List<Product> = result
                mutableProduct.postValue(list) //=mutableProduct.value=list
                //The difference is that postValue can work in the background
                //so if you're in the UI use .value
            } else{
                mutableMessage.postValue("An Error Occurred.\nPlease try again later")
            }
        }
    }

    fun addToFavorite(product: Product){
        if(product!=null) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val result = repo.insertProduct(product)
                    if (result > 0) {
                        mutableMessage.postValue("Product Added Successfully")
                    } else {
                        mutableMessage.postValue("Product is already in favorite!")

                    }
                } catch (e: Exception) {
                    mutableMessage.postValue("Couldn't add product, ${e.message}")
                }
            }
        }else{
            mutableMessage.postValue("Couldn't Add Product, Missing product")
        }
    }

    fun getFavoriteProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getAllProducts(false)
            if (result != null) {
                mutableFavoriteProducts.postValue(result)
            }
        }
    }

    fun deleteFavorite(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repo.deleteProduct(product)
                if (result > 0) {
                    mutableMessage.postValue("Product Removed Successfully")
                    getFavoriteProducts() // Refresh the list
                }
            } catch (e: Exception) {
                mutableMessage.postValue("Couldn't remove product, ${e.message}")
            }
        }
    }
}
 */
