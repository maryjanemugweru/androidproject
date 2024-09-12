package com.example.androidproject.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.androidproject.screen.RegisterScreen
import com.example.androidproject.screen.LoginScreen

@Composable
fun NavGraph(
    navController: NavHostController
)

{
    NavHost(
        navController = navController ,
        startDestination = Screens.LoginScreen.route
    ){
        // register screen
        composable(
            route = Screens.RegisterScreen.route
        ){
            RegisterScreen(navController = navController)
        }

        // login screen
        composable(
            route = Screens.LoginScreen.route
        ){
            LoginScreen(navController = navController)
        }
    }
}