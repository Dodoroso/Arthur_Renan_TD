package com.example.arthur_renan_td

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.arthur_renan_td.Entity.Product
import androidx.compose.ui.Modifier
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                val context = LocalContext.current
                Button(onClick={val intent = Intent(context, DetailsProduct::class.java)
                    context.startActivity(intent)}){
                    Text(text = "Détails")
                }
                MyAppBarWithActions()
                PageProduit()
            }

        }
    }
}


@Composable
fun PageProduit() {
    val produits = remember { mutableStateOf<List<Product>?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
                val apiRecupProduits = RetrofitInstance.api.getProducts()
                produits.value = apiRecupProduits

        }
    }

    Column {
        if (produits.value != null) {
            LazyColumn {
                items(produits.value!!) { product ->
                    ProductCard(product = product)
                }
            }
        } else {
            Text(text = "Chargement des données...", modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun MyAppBarWithActions() {
    TopAppBar(
        backgroundColor = Color.Blue,
        contentColor = Color.White,
        elevation = 8.dp,
        title = { Text("My App") },
        actions = {
            IconButton(onClick = { /* Do something */ }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        }
    )
}

@Composable
fun ProductCard(product: Product) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = product.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Prix: ${product.price} €",
                fontSize = 16.sp,
                color = Color.Gray
            )
            Text(
                text = product.description,
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }
    }
}





