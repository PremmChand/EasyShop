package com.example.easyshop.pages

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TopAppBar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easyshop.AppUtil
import com.example.easyshop.GlobalNavigation
import com.example.easyshop.R
import com.example.easyshop.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun ProfilePage(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val navController = GlobalNavigation.navController
    val userModel = remember { mutableStateOf(UserModel()) }
    var addressInput by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
            .get()
            .addOnSuccessListener { document ->
                document.toObject(UserModel::class.java)?.let { result ->
                    userModel.value = result
                    addressInput = result.address
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                },
                backgroundColor = MaterialTheme.colorScheme.primary
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Profile image
                Image(
                    painter = painterResource(id = R.drawable.cat),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .height(160.dp)
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                // Name
                Text(
                    text = userModel.value.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Address field
                Text(
                    text = "Shipping Address",
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = addressInput,
                    onValueChange = { addressInput = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    textStyle = TextStyle(fontSize = 14.sp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                        onDone = {
                            if (addressInput.isNotEmpty()) {
                                Firebase.firestore.collection("users")
                                    .document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                                    .update("address", addressInput)
                                    .addOnSuccessListener {
                                        AppUtil.showToast(context, "Address updated successfully")
                                    }
                            } else {
                                AppUtil.showToast(context, "Address can't be empty")
                            }
                        }
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Email
                Text(
                    text = "Email:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = userModel.value.email,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                // Cart item count
                Text(
                    text = "Items in Cart: ${userModel.value.cartItems.values.sum()}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                // View Orders
                Text(
                    text = "View My Orders",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("orders")
                        }
                        .padding(vertical = 12.dp)
                )
            }

            // Sign Out Button
            Button(
                onClick = {
                    logoutUser(context) {
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text("Sign out", fontSize = 16.sp)
            }
        }
    }
}

fun logoutUser(context: Context, onLogout: () -> Unit) {
    FirebaseAuth.getInstance().signOut()
    Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
    onLogout()
}
