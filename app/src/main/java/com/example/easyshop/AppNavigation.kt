package com.example.easyshop

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.easyshop.pages.CategoryProductPage
import com.example.easyshop.pages.CheckoutPage
import com.example.easyshop.pages.ProductDetailsPage
import com.example.easyshop.screen.AuthScreen
import com.example.easyshop.screen.HomeScreen
import com.example.easyshop.screen.LoginScreen
import com.example.easyshop.screen.SignupScreen
import com.example.easyshop.viewmodel.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AppNavigation(modifier: Modifier=Modifier){
    val navController = rememberNavController()
    GlobalNavigation.navController = navController
    val authViewModel: AuthViewModel = viewModel()
    val isLoggedIn = Firebase.auth.currentUser!=null
    val firstPage = if(isLoggedIn) "home" else "auth"
   NavHost(navController=navController, startDestination = firstPage) {
       composable("auth") {
           AuthScreen(modifier,navController)
       }
       composable("login") {
           LoginScreen(modifier,navController)
       }
       composable("signup",) {
           SignupScreen(modifier,navController,authViewModel)
       }
       composable("home",) {
          HomeScreen(modifier,navController,authViewModel)
       }
       composable("category-products/{categoryId}") {
           val categoryId = it.arguments?.getString("categoryId")
           CategoryProductPage(modifier,categoryId?:"")
       }
       composable("product-details/{productId}") {
           val productId = it.arguments?.getString("productId")
           ProductDetailsPage(modifier,productId?:"")
       }

       composable("checkout") {
           CheckoutPage(modifier)
       }
   }
}

object GlobalNavigation{
    lateinit var navController:NavHostController
}