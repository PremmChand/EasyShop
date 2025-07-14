package com.example.easyshop.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.easyshop.components.ProductCardReadOnly
import com.example.easyshop.components.ProductItemView
import com.example.easyshop.model.ProductModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritePage(modifier: Modifier = Modifier) {
    val favorites = remember { mutableStateListOf<ProductModel>() }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    LaunchedEffect(Unit) {
        // Fetch favorite product IDs
        Firebase.firestore.collection("users").document(userId).get()
            .addOnSuccessListener { userDoc ->
                val favMap = userDoc.get("favorites") as? Map<String, Boolean>
                val favIds = favMap?.filterValues { it }?.keys?.toList() ?: emptyList()

                if (favIds.isNotEmpty()) {
                    Firebase.firestore.collection("banners")
                        .document("stock").collection("products")
                        .whereIn("id", favIds)
                        .get()
                        .addOnSuccessListener { snapshot ->
                            val products = snapshot.toObjects(ProductModel::class.java)
                            favorites.clear()
                            favorites.addAll(products)
                        }
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Favorites") })
        }
    ) { paddingValues ->
        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("You have no favorites yet.")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(favorites.size) { index ->
                    ProductCardReadOnly(product = favorites[index])
                }
            }
        }
    }
}
