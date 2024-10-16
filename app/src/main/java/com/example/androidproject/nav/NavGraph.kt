package com.example.androidproject.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.androidproject.screen.AdminApplicationListScreen
import com.example.androidproject.screen.DashboardScreen
import com.example.androidproject.screen.EditJobScreen
import com.example.androidproject.screen.JobApplicationScreen
import com.example.androidproject.screen.JobDetailsScreen
import com.example.androidproject.screen.JobPostScreen
import com.example.androidproject.screen.JobScreen
import com.example.androidproject.screen.RegisterScreen
import com.example.androidproject.screen.LoginScreen
import com.example.androidproject.screen.UserApplicationsScreen
import com.example.androidproject.utils.DashboardViewModel
import com.example.androidproject.utils.JobViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph(
    navController: NavHostController,
    dashboardViewModel: DashboardViewModel,
    jobViewModel: JobViewModel
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
        
        composable(
            route =Screens.JobPostScreen.route
        ){
            JobPostScreen(navController = navController, jobViewModel = jobViewModel)
        }
        composable(
            route = Screens.JobDetailsScreen.route
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: return@composable

            JobDetailsScreen(
                navController = navController,
                jobID = jobId,
                jobViewModel = jobViewModel
            )

        }

        composable(
            route = Screens.EditJobScreen.route
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: return@composable

            EditJobScreen(
                navController = navController,
                jobID = jobId,
                jobViewModel = jobViewModel
            )

        }

        composable(
            route = Screens.JobApplicationScreen.route
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: return@composable

            // Get the currently authenticated user's ID
            val currentUser = FirebaseAuth.getInstance().currentUser
            val userID = currentUser?.uid ?: return@composable

            JobApplicationScreen(
                navController = navController,
                jobID = jobId,
                jobViewModel = jobViewModel,
                userId = userID
            )
        }

        composable(
            route = Screens.UserApplicationsScreen.route
        ){
            UserApplicationsScreen(navController = navController)
        }

        composable(
            route = Screens.AdminApplicationListScreen.route
        ){
            AdminApplicationListScreen(navController = navController)
        }

    }
}
