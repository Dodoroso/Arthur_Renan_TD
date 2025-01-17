package com.example.arthur_renan_td

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arthur_renan_td.Entity.Product
import kotlinx.coroutines.launch

class DetailsProduct : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Récupération de l'ID du produit depuis l'Intent
        val productId = intent.getIntExtra("product_id", -1)

        setContent {
            ProductDetailPage(productId)
        }
    }
}

@Composable
fun ProductDetailPage(productId: Int) {
    val product = remember { mutableStateOf<Product?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Charger les détails du produit via Retrofit
    LaunchedEffect(productId) {
        coroutineScope.launch {
            val apiProductDetail = RetrofitInstance.api.getProductById(productId)
            product.value = apiProductDetail
        }
    }

    // Afficher les détails du produit
    if (product.value != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LoadImageFromUrl(product.value!!.image)
            Text(
                text = product.value!!.title,
                modifier = Modifier.padding(top = 8.dp),
                fontSize = 20.sp
            )
            Text(
                text = product.value!!.description,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "Prix: ${product.value!!.price} €",
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    } else {
        Text(
            text = "Chargement des détails du produit...",
            modifier = Modifier.padding(16.dp)
        )
    }
}
