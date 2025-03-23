package com.mariammuhammad.mvvmproducts.View

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.mariammuhammad.mvvmproducts.Model.DTO.Product
import com.mariammuhammad.mvvmproducts.Model.ProductRepository
import com.mariammuhammad.mvvmproducts.Model.Remote.ProductRemoteDataSource
import com.mariammuhammad.mvvmproducts.Model.Remote.RetrofitHelper
import com.mariammuhammad.mvvmproducts.Model.local.ProductLocalDataSource
import com.mariammuhammad.mvvmproducts.Model.local.ProductRoomDatabase
import com.mariammuhammad.mvvmproducts.View.ui.theme.MVVMProductsTheme
import com.mariammuhammad.mvvmproducts.ViewModel.AllProductsFactory
import com.mariammuhammad.mvvmproducts.ViewModel.ProductViewModel

class ProductsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            AllProductScreen(
                ViewModelProvider(
                    this, AllProductsFactory(
                        ProductRepository(
                            ProductRemoteDataSource(RetrofitHelper.service),
                            ProductLocalDataSource(
                                ProductRoomDatabase.getInstance(this@ProductsActivity)
                                    .productDao())
                        )
                    )
                ).get(ProductViewModel::class.java)
            )

            AllProductScreen(viewModel = viewModel())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllProductScreen(viewModel: ProductViewModel) {
    val productState = viewModel.products.collectAsState()
    val messageState = viewModel.message.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.getProducts()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("All Products") },
                navigationIcon = {
                    IconButton(onClick = {  }) { //finish() doesn't work and idk why
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            LazyColumn {
                items(productState.value?.size ?: 0) { index ->
                    ProductRow(productState.value?.get(index), "Favorite") {
                        productState.value?.get(index)?.let { product -> //extension function won't work if the object is null
                            viewModel.addToFavorite(product)
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(messageState.value) {
        if (!messageState.value.isNullOrBlank()) {
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
                        .border(2.dp, color = Color.Black, shape = RoundedCornerShape(2.dp))
                        .clip(RoundedCornerShape(2.dp))

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

            if (product != null) {
                Text(
                    text = "Category: ${product.category}", fontSize = 16.sp, fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                )
            }
            if (product != null) {
                Text(text = product.description, fontSize = 16.sp)
            }
            if (product != null) {
                Text(text = "Price: $${product.price}", fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top= 5.dp, bottom = 5.dp))

            }
            Button(onClick = action,  colors = ButtonDefaults.buttonColors( Color.Black)) {
                Text("Favorite")
            }

        }
    }
}