package com.example.easyshop

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.easyshop.ui.theme.EasyShopTheme
import com.razorpay.PaymentResultListener

/*
* This app is made following Youtube resource:->
Easy Tuto
*
* */



class MainActivity : ComponentActivity(),PaymentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            EasyShopTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(Modifier.padding(innerPadding))
                }
            }
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        AppUtil.clearCartAndAddToOrders()
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Payment Success")
            .setMessage("Thank You! Your payment is successfully completed and your order has been placed")
            .setPositiveButton("OK"){_,_ ->
                val navController = GlobalNavigation.navController
                navController.popBackStack()
                navController.navigate("home")
            }
            .setCancelable(false)
            .show()

    }

    override fun onPaymentError(p0: Int, p1: String?) {
       AppUtil.showToast(this, "Payment Failed")
    }
}
