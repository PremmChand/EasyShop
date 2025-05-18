package com.example.easyshop.pages


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easyshop.GlobalNavigation
import com.example.easyshop.components.CartItemView
import com.example.easyshop.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun CartPage(modifier: Modifier = Modifier) {
    val userModel = remember { mutableStateOf(UserModel()) }

    DisposableEffect(Unit) {
        val listener = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
            .addSnapshotListener { it, _ ->
                it?.toObject(UserModel::class.java)?.let { result ->
                    userModel.value = result
                }
            }
        onDispose { listener.remove() }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Your cart",
            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        val isCartEmpty = userModel.value.cartItems.isEmpty()

        if (isCartEmpty) {
            // Empty cart message
            Text(
                text = "Your cart is empty",
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            )
        } else {
            // Product list
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(userModel.value.cartItems.toList(), key = { it.first }) { (productId, qty) ->
                    CartItemView(productId = productId, qty = qty)
                }
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(top = 8.dp),
            onClick = {
                if (!isCartEmpty) {
                    GlobalNavigation.navController.navigate("checkout")
                }
            }
        ) {
            Text(
                "Checkout",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
