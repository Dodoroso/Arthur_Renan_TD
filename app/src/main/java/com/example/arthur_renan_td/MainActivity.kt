package com.example.arthur_renan_td

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.Text
import coil.compose.rememberAsyncImagePainter
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
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                val context = LocalContext.current
                MyAppBarWithActions()
                PageProduit()
            }

        }
    }
}


@Composable
fun PageProduit() {
    val produits = remember { mutableStateOf<List<Product>?>(null) }
    val categories = remember { mutableStateOf<List<String>>(emptyList()) }
    val selectedCategory = remember { mutableStateOf("All") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            // Récupérer les produits
            val apiRecupProduits = RetrofitInstance.api.getProducts()
            produits.value = apiRecupProduits

            // Récupérer les catégories
            val apiCategories = RetrofitInstance.api.getCategories()
            categories.value = listOf("All") + apiCategories
        }
    }

    Column {
        // Composant Dropdown pour les catégories
        if (categories.value.isNotEmpty()) {
            CategoryDropdown(
                categories = categories.value,
                selectedCategory = selectedCategory.value,
                onCategorySelected = { category ->
                    selectedCategory.value = category
                }
            )
        }

        // Filtrer les produits
        val filteredProduits = produits.value?.filter {
            selectedCategory.value == "All" || it.category == selectedCategory.value
        }

        // Afficher les produits
        if (filteredProduits != null) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredProduits) { product ->
                    ProductCard(product = product)
                }
            }
        } else {
            Text(text = "Chargement des données...", modifier = Modifier.padding(16.dp))
        }
    }
}




@Composable
fun CategoryDropdown(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Bouton de sélection
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray, RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = selectedCategory,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Image(
                painter = rememberAsyncImagePainter("https://upload.wikimedia.org/wikipedia/commons/7/7e/Black_down_arrow.png"),
                contentDescription = "Dropdown Icon",
                modifier = Modifier.size(16.dp)
            )
        }

        // Liste déroulante
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            categories.forEach { category ->
                androidx.compose.material.DropdownMenuItem(onClick = {
                    onCategorySelected(category)
                    expanded = false
                }) {
                    Text(
                        text = category,
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}


@Composable
fun MyAppBarWithActions() {
    val context = LocalContext.current

    TopAppBar(
        backgroundColor = Color.Blue,
        contentColor = Color.White,
        elevation = 8.dp,
        title = { Text("My App") },
        actions = {
            // Bouton de recherche (existant)
            IconButton(onClick = { /* Do something */ }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }

            // Bouton pour ouvrir la page "Panier"
            IconButton(onClick = {
                val intent = Intent(context, Paniers::class.java)
                context.startActivity(intent)
            }) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Rounded.ShoppingCart,
                    contentDescription = "Panier"
                )
            }
        }
    )
}


@Composable
fun ProductCard(product: Product, context: android.content.Context = LocalContext.current) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .padding(8.dp)
            .clickable {
                // Naviguer vers la page de détails du produit
                val intent = Intent(context, DetailsProduct::class.java).apply {
                    putExtra("product_id", product.id) // Passe l'ID du produit
                }
                context.startActivity(intent)
            }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            LoadImageFromUrl(product.image)
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
        }
    }
}


@Composable
fun LoadImageFromUrl(url: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = url,
        contentDescription = "Translated description of what the image contains"
    )
}



