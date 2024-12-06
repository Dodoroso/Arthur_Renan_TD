package com.example.arthur_renan_td

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Card


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PageProduit()
        }
    }
}

@Composable
fun PageProduit() {
    val Produit = remember { mutableStateOf<Product?>(null) }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val APIRecupProduit = RetrofitInstance.api.getProduct()
            Produit.value = APIRecupProduit
        }
    }

    Column(){
       MyAppBarWithActions()
        Text(text = "test")
        Text(text = "test2")
    }

//    Row(){
//        if (Produit.value != null) {
//            val product = Produit.value!!
//            Text(text = "Titre: ${product.title}\nDescription: ${product.description}\nPrix: ${product.price} €")
//        } else {
//            Text(text = "Chargement des données...")
//        }
//    }
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
fun SimpleCard(){
    val paddingModifier  = Modifier.padding(10.dp)
    Card(
        modifier = paddingModifier
    ) {
        Text(text = "Simple Card with elevation",
            modifier = paddingModifier)
    }
}





