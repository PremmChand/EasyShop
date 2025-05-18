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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.easyshop.components.ProductCardReadOnly
import com.example.easyshop.components.ProductItemView
import com.example.easyshop.model.ProductModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritePage(modifier: Modifier = Modifier) {

    val favorites = remember { mutableStateListOf<ProductModel>() }

    // TODO: Fetch favorites from Firestore or local storage

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
