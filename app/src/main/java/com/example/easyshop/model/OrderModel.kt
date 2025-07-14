package com.example.easyshop.model

import com.google.firebase.Timestamp
import com.google.rpc.Status

data class OrderModel(
    val id :String = "",
    val date : Timestamp = Timestamp.now(),
    val userId:String ="",
    val items:Map<String,Long> = mapOf(),
    val status: String = "",
    val address:String= ""

)
