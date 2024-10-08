package com.example.androidproject.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.androidproject.screen.DashboardScreen
import com.example.androidproject.screen.JobScreen
import com.example.androidproject.screen.RegisterScreen
import com.example.androidproject.screen.LoginScreen
import com.example.androidproject.utils.DashboardViewModel
import com.example.androidproject.utils.JobViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    dashboardViewModel: DashboardViewModel,
    jobViewModel: JobViewModel
)

{
    NavHost(
        navController = navController ,
        startDestination = Screens.DashboardScreen.route
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

        // dashboard screen
        composable(
            route = Screens.DashboardScreen.route
        ){
            DashboardScreen(navController = navController, dashboardViewModel = dashboardViewModel)
        }

        composable(
            route = Screens.JobScreen.route
        ){
            JobScreen(navController = navController, jobViewModel = jobViewModel)
        }
    }
}