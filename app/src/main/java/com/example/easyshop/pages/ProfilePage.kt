package com.example.easyshop.pages


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easyshop.GlobalNavigation

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfilePage(modifier: Modifier) {
    val context = LocalContext.current
    val navController = GlobalNavigation.navController

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Profile") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to your profile!", fontSize = 20.sp)

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                logoutUser(context) {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            }) {
                Text("Logout")
            }
        }
    }
}


fun logoutUser(context: Context, onLogout: () -> Unit) {
    FirebaseAuth.getInstance().signOut()
    Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
    onLogout()
}

