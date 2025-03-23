package com.mariammuhammad.mvvmproducts.View

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.mariammuhammad.mvvmproducts.Model.DTO.Product
import com.mariammuhammad.mvvmproducts.Model.ProductRepository
import com.mariammuhammad.mvvmproducts.Model.Remote.ApiCall
import com.mariammuhammad.mvvmproducts.Model.Remote.ProductRemoteDataSource
import com.mariammuhammad.mvvmproducts.Model.Remote.RetrofitHelper
import com.mariammuhammad.mvvmproducts.Model.local.ProductLocalDataSource
import com.mariammuhammad.mvvmproducts.Model.local.ProductRoomDatabase
import com.mariammuhammad.mvvmproducts.ViewModel.AllProductsFactory
import com.mariammuhammad.mvvmproducts.ViewModel.ProductViewModel
import com.mariammuhammad.mvvmproducts.ui.theme.MVVMProductsTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    // var productList = remember { mutableStateOf(emptyList<Product>()) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //  var productList = remember { mutableStateOf(listOf<Product>()) }
//            AllProductScreen(
//                ViewModelProvider(
//                    this, AllProductsFactory(
//                        ProductRepository(
//                            ProductRemoteDataSource(RetrofitHelper.service),
//                            ProductLocalDataSource(
//                                ProductRoomDatabase.getInstance(this@MainActivity)
//                                    .productDao())
//                        )
//                    )
//                ).get(ProductViewModel::class.java)
//            )
            MainScreen(
                onAllProductsClick = {
                    val intent = Intent(this, ProductsActivity::class.java)
                    startActivity(intent)
                },
                onAllFavoritesClick = {
                    val intent = Intent(this, FavoritesActivity::class.java)
                    startActivity(intent)
                },
                onExitClick = {
                    finish()
                }
            )
        }
    }
}

@Composable
fun MainScreen(
    onAllProductsClick: () -> Unit,
    onAllFavoritesClick: () -> Unit,
    onExitClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onAllProductsClick,
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            colors = ButtonDefaults.buttonColors(Color.Black)
        ) {
            Text("All Products")
        }

        Button(
            onClick = onAllFavoritesClick,
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            colors = ButtonDefaults.buttonColors(Color.Black)
        ) {
            Text("All Favorites")
        }

        Button(
            onClick = onExitClick,
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            colors = ButtonDefaults.buttonColors(Color.Black)
        ) {
            Text("Exit")
        }
    }
}


/*
@Composable

fun MainScreen(
    onAllProductsClick: () -> Unit,
    onAllFavoritesClick: () -> Unit,
    onExitClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Button for All Products
        Button(
            onClick = onAllProductsClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("All Products")
        }

        // Button for All Favorites
        Button(
            onClick = onAllFavoritesClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("All Favorites")
        }

        // Button for Exit
        Button(
            onClick = onExitClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Exit")
        }
    }
}

//pregunte ch para entender todo sobre eso
//@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun AllProductScreen(viewModel: ProductViewModel) {
    val productState = viewModel.products.observeAsState()
    val messageState = viewModel.message.observeAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }

    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        )
        {
            LazyColumn {
                items(productState.value?.size ?: 0) { it: Int ->
                    ProductRow(productState.value?.get(it), "Favorite",
                        //{viewModel.addToFavorite(productState.value?.get(it))}
                        {
                            productState.value?.get(it)?.let { it1 -> viewModel.addToFavorite(it1) }
                            /* scope.launch {
                                 delay(100)
                                 if(messageState.value!=null)
                                     snackbarHostState.showSnackbar(
                                         message = messageState.value.toString(),
                                         duration = SnackbarDuration.Short
                                     )
                             }
                             */
                        })
                }
            }
        }
        LaunchedEffect(messageState.value) {
            if (!messageState.value.isNullOrBlank())
                snackbarHostState.showSnackbar(
                    message = messageState.value.toString(),
                    duration = SnackbarDuration.Short
                )
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductRow(product: Product?, actionName: String, action: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color.Black)
            .background(color = Color.LightGray)
            .padding(10.dp),
        //   .clickable { onClick(product) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (product != null) {
            if (product.thumbnail.isNotEmpty()) {
                Log.i("TAG", "ProductUi: \"Thumbnail${product.thumbnail}")
                GlideImage(
                    model = product.thumbnail,
                    contentDescription = product.description,
                    modifier = Modifier.size(100.dp)
                )
            } else {
                CircularProgressIndicator()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(14.dp),
        ) {
            if (product != null) {
                Text(text = product.title, fontSize = 20.sp)
            }
//            Text(text = product.description, fontSize = 16.sp)
        }

        Row {
            Button(onClick = {}) {
                Text("Favorite")
            }
        }
    }
}
*/