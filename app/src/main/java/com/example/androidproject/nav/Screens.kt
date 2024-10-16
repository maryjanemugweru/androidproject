package com.example.androidproject.nav

sealed class Screens(val route:String) {
    data object RegisterScreen:Screens(route = "register_screen")
    data object LoginScreen:Screens(route = "login_screen")
    data object DashboardScreen:Screens(route = "dashboard_screen")
    data object JobScreen:Screens(route = "job_screen")
    data object JobPostScreen:Screens(route ="jobPosting_screen" )
    data object JobDetailsScreen:Screens(route = "jobDetails/{jobId}")
    data object EditJobScreen: Screens(route = "editJobs_screen/{jobId}")
    data object JobApplicationScreen:Screens(route = "jobApplication/{jobId}")
    data object UserApplicationsScreen:Screens(route = "userApplications_screen")
    data object AdminApplicationListScreen:Screens(route = "adminApplication_screen")


}