package com.example.easyshop

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.razorpay.Checkout
import org.json.JSONObject
import com.example.easyshop.BuildConfig
import com.example.easyshop.model.OrderModel
import com.google.firebase.Timestamp
import java.util.UUID

object AppUtil {
    fun showToast(context: Context,message:String){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }

    fun addItemToCart(context: Context,productId:String){
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnCompleteListener{
            if(it.isSuccessful){
                val currentCart = it.result.get("cartItems") as? Map<String,Long>?: emptyMap()
                val currentQuantity = currentCart[productId]?:0
                val updatedQuantity = currentQuantity + 1;
                val updatedCart = mapOf("cartItems.$productId" to updatedQuantity)

                userDoc.update(updatedCart)
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            showToast(context,"Item added to the cart")
                        }else{
                            showToast(context, "Failed adding item to the cart")
                        }
                    }
            }
        }
    }

    fun removeFromCart(context: Context,productId:String,removeAll:Boolean=false){
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnCompleteListener{
            if(it.isSuccessful){
                val currentCart = it.result.get("cartItems") as? Map<String,Long>?: emptyMap()
                val currentQuantity = currentCart[productId]?:0
                val updatedQuantity = currentQuantity - 1;
                val updatedCart =
                        if(updatedQuantity <=0 || removeAll)
                    mapOf("cartItems.$productId" to FieldValue.delete())
                else
                            mapOf("cartItems.$productId" to updatedQuantity)

                userDoc.update(updatedCart)
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            showToast(context,"Item removed from the cart")
                        }else{
                            showToast(context, "Failed removing item to the cart")
                        }
                    }
            }
        }
    }

    fun clearCartAndAddToOrders(){
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
        userDoc.get().addOnCompleteListener{
            if(it.isSuccessful){
                val currentCart = it.result.get("cartItems") as? Map<String,Long>?: emptyMap()
                val order = OrderModel(
                    id = "ORD_" + UUID.randomUUID().toString().replace("-","").take(10).uppercase(),
                    userId =  FirebaseAuth.getInstance().currentUser?.uid!!,
                    date = Timestamp.now(),
                    items = currentCart,
                    status = "ORDERED",
                    address = it.result.get("address") as String
                )

                Firebase.firestore.collection("orders")
                    .document(order.id).set(order)
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            userDoc.update("cartItems",FieldValue.delete()
                            )
                        }
                    }

            }
        }

    }

    fun getDiscountPercentage() : Float {
        return  10.0f
    }

    fun getTaxPercentage() : Float {
        return  13.0f
    }

    fun startPayment(amount: Float){
            val checkout  = Checkout()
            checkout.setKeyID(BuildConfig.RAZORPAY_KEY_ID)

        val options = JSONObject().apply {
            put("name", "Easy Shop")
            put("description", "Order Payment")
            put("currency", "INR")
            put("amount", (amount * 100).toInt())
            put("prefill", JSONObject().apply {
                put("email", "test@example.com")
                put("contact", "9849589898")
            })
        }

        checkout.open(GlobalNavigation.navController.context as Activity, options)
    }



}