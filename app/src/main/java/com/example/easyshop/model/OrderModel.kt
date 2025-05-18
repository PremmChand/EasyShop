package com.example.easyshop.model

data class OrderModel(
    val userId: String = "",
    val userName: String = "",
    val address: String = "",
    val items: Map<String, Long> = emptyMap(),
    val totalAmount: Float = 0f,
    val status: String = "Placed",
    val timestamp: Long = System.currentTimeMillis()
)
