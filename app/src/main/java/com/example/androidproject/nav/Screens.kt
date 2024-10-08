package com.example.androidproject.nav

sealed class Screens(val route:String) {
    data object RegisterScreen:Screens(route = "register_screen")
    data object LoginScreen:Screens(route = "login_screen")
    data object DashboardScreen:Screens(route = "dashboard_screen")
    data object JobScreen:Screens(route = "job_screen")
}