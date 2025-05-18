package com.example.easyshop.pages


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.easyshop.components.ProductItemView
import com.example.easyshop.model.ProductModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun CategoryProductPage(
    modifier: Modifier = Modifier,
    categoryId: String,
){
    val productsList = remember {
        mutableStateOf<List<ProductModel>>(emptyList())
    }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("banners").document("stock")
            .collection("products")
            .whereEqualTo("category",categoryId)
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

    LazyColumn {
        items(productsList.value.chunked(2)){rowItems->
           Row{
               rowItems.forEach{
                ProductItemView(product = it , modifier = Modifier.weight(1f) )
               }
               if(rowItems.size==1){
                   Spacer(modifier = Modifier.weight(1f))
                   }
           }
        }
    }
}