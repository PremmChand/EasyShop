package com.example.easyshop.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.easyshop.GlobalNavigation
import com.example.easyshop.model.OrderModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderPage(modifier: Modifier = Modifier) {
    val ordersList = remember { mutableStateOf<List<OrderModel>>(emptyList()) }
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("orders")
            .whereEqualTo("userId", currentUserId)
            .get()
            .addOnSuccessListener { snapshot ->
                val result = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(OrderModel::class.java)
                }
                ordersList.value = result.sortedByDescending { it.date }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Orders") },
                navigationIcon = {
                    IconButton(onClick = {
                        GlobalNavigation.navController.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (ordersList.value.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("No orders found.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(ordersList.value) { order ->
                    OrderCard(order = order)
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: OrderModel) {
    val formattedDate = remember(order.date) {
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        sdf.format(order.date.toDate())
    }

    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Order ID: ${order.id}", style = MaterialTheme.typography.titleMedium)
            Text("Date: $formattedDate")
            Text("Address: ${order.address}")
            Text("Status: ${order.status}", color = getStatusColor(order.status))
            Text("Items: ${order.items.size}")
        }
    }
}

@Composable
fun getStatusColor(status: String): Color {
    return when (status.lowercase()) {
        "placed" -> MaterialTheme.colorScheme.primary
        "shipped" -> Color.Blue
        "delivered" -> Color(0xFF2E7D32) // Green
        "cancelled" -> Color.Red
        else -> Color.Gray
    }
}
