package com.example.easyshop.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.easyshop.AppUtil
import com.example.easyshop.GlobalNavigation.navController
import com.example.easyshop.model.ProductModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProductDetailsPage(modifier: Modifier = Modifier, productId: String) {
    var product by remember { mutableStateOf(ProductModel()) }
    val context = LocalContext.current
    val pagerState = rememberPagerState(initialPage = 0)
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("banners").document("stock")
            .collection("products")
            .document(productId).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result.toObject(ProductModel::class.java)
                    if (result != null) {
                        product = result
                    }
                }
            }
    }

    Scaffold(
        topBar = {
            androidx.compose.material.TopAppBar(
                title = {
                    androidx.compose.material.Text(
                        text = "Products Details",
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    androidx.compose.material.IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        androidx.compose.material.Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                backgroundColor = Color.White,
                elevation = 4.dp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Your full existing content below
            Text(
                text = product.title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (product.images.isNotEmpty()) {
                HorizontalPager(
                    count = product.images.size,
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) { page ->
                    AsyncImage(
                        model = product.images[page],
                        contentDescription = "Product Image $page",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                    )
                }

                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    activeColor = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$" + product.price,
                    fontSize = 16.sp,
                    style = TextStyle(textDecoration = TextDecoration.LineThrough)
                )
                Text(
                    text = "$" + product.actualPrice,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))

//                IconButton(onClick = { }) {
//                    Icon(
//                        imageVector = Icons.Default.FavoriteBorder,
//                        contentDescription = "Add to Favourite"
//                    )
//                }

                IconButton(onClick = {
                    val favRef = Firebase.firestore.collection("users").document(userId)

                    favRef.get().addOnSuccessListener { doc ->
                        val favorites = doc.get("favorites") as? Map<String, Boolean> ?: mapOf()

                        val isFav = favorites[productId] == true
                        val updatedFavorites = if (isFav) {
                            favorites - productId // remove favorite
                        } else {
                            favorites + (productId to true) // add favorite
                        }

                        favRef.update("favorites", updatedFavorites)
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder, // or Icons.Filled.Favorite if already favorited
                        contentDescription = "Toggle Favorite"
                    )
                }

            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    AppUtil.addItemToCart(context, productId)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Add to Cart", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Product Description :",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = product.description, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))

            if (product.otherDetails.isNotEmpty()) {
                Text(
                    text = "Other Product Details :",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                product.otherDetails.forEach { (key, value) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "$key : ",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(text = value, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
