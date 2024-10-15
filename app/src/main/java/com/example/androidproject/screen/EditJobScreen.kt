package com.example.androidproject.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.androidproject.utils.JobData
import com.example.androidproject.utils.JobViewModel

@Composable
fun EditJobScreen(navController: NavController, jobViewModel: JobViewModel, jobId: String) {
    // State variables for job data
    var jobTitle by remember { mutableStateOf("") }
    var jobCompany by remember { mutableStateOf("") }
    var jobLocation by remember { mutableStateOf("") }
    var jobDescription by remember { mutableStateOf("") }

    // Fetch job details
    LaunchedEffect(jobId) {
        jobViewModel.getJobDetails(jobId) // Fetch job details using the ViewModel
    }

    // Observe job details
    val jobDetails by jobViewModel.jobDetails.collectAsState()

    // Update state variables when job details are fetched
    jobDetails?.let { job ->
        jobTitle = job.title
        jobCompany = job.company
        jobLocation = job.location
        jobDescription = job.description
    }

    // UI Layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Edit Job", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))

        // Input fields
        TextField(value = jobTitle, onValueChange = { jobTitle = it }, label = { Text("Job Title") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = jobCompany, onValueChange = { jobCompany = it }, label = { Text("Company") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = jobLocation, onValueChange = { jobLocation = it }, label = { Text("Location") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = jobDescription, onValueChange = { jobDescription = it }, label = { Text("Description") })
        Spacer(modifier = Modifier.height(16.dp))

        // Update button
        Button(onClick = {
            if (jobTitle.isNotEmpty() && jobCompany.isNotEmpty() && jobLocation.isNotEmpty() && jobDescription.isNotEmpty()) {
                val updatedJob = JobData(jobID = jobId, title = jobTitle, company = jobCompany, location = jobLocation, description = jobDescription)
                jobViewModel.saveJob(updatedJob, navController.context)
                navController.popBackStack() // Navigate back to the previous screen
            } else {
                Toast.makeText(navController.context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Update Job")
        }
    }
}
