package com.example.androidproject.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.androidproject.utils.JobData
import com.example.androidproject.utils.JobViewModel

@Composable
fun JobDetailsScreen(
    navController: NavController,
    jobId: String,
    onEditClicked: () -> Unit
) {
    val jobViewModel: JobViewModel = viewModel()
    val context = LocalContext.current

    // Fetch job details
    LaunchedEffect(jobId) {
        jobViewModel.getJobDetails(jobId) // Fetch job details using the ViewModel
    }

    // Observe job details
    val jobDetails by jobViewModel.jobDetails.collectAsState()

    // Check if job details are null
    if (jobDetails == null) {
        // Show a loading indicator or a placeholder
        Text(
            text = "Loading job details...",
            modifier = Modifier.fillMaxSize(),
            fontSize = 16.sp
        )
        return
    }

    // Display job details once they are fetched
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Display Job Data
        Text(
            text = "Job Title: ${jobDetails.title}",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Text(text = "Company: ${jobDetails.company}", fontSize = 18.sp)
        Text(text = "Location: ${jobDetails.location}", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Description: ${jobDetails.description}", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Edit button
        Button(onClick = onEditClicked) {
            Text("Edit Job")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Delete button
        Button(onClick = {
            jobViewModel.deleteJob(jobId, context)
            navController.popBackStack() // Navigate back after deletion
        }) {
            Text("Delete Job")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Apply Button
        Button(onClick = { handleApply(context, jobDetails) }) {
            Text("Apply")
        }
    }
}

// Function to handle job application logic
private fun handleApply(context: android.content.Context, job: JobData) {
    Toast.makeText(context, "Applied for ${job.title}", Toast.LENGTH_SHORT).show()
}
