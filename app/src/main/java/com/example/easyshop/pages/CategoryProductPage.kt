package com.example.easyshop.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.easyshop.components.ProductItemView
import com.example.easyshop.model.ProductModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun CategoryProductPage(
    navController: NavController,
    modifier: Modifier = Modifier,
    categoryId: String,
) {
    val productsList = remember {
        mutableStateOf<List<ProductModel>>(emptyList())
    }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("banners").document("stock")
            .collection("products")
            .whereEqualTo("category", categoryId)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val resultList = it.result.documents.mapNotNull { doc ->
                        doc.toObject(ProductModel::class.java)
                    }
                    productsList.value = resultList.plus(resultList).plus(resultList)
                }
            }
    }

    Scaffold(

        topBar = {
            androidx.compose.material.TopAppBar(
                title = {
                    Text(
                        text = "Category Products",
                        color = Color.Black // Text color
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        androidx.compose.material.Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = Color.Black // Icon color
                        )
                    }
                },
                backgroundColor = Color.White, // App bar background
                elevation = 4.dp,
                modifier = Modifier.padding(top = 16.dp) // Top margin
            )
        }

    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            items(productsList.value.chunked(2)) { rowItems ->
                Row(modifier = Modifier.padding(8.dp)) {
                    rowItems.forEach {
                        ProductItemView(product = it, modifier = Modifier.weight(1f))
                    }
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
