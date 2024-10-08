package com.example.androidproject.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
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
fun JobDetailsScreen(navController: NavController, jobId: String) {
    val jobViewModel: JobViewModel = viewModel()
    jobViewModel.getJobDetails(jobId) // Trigger fetching job details
    val jobState = jobViewModel.jobDetails.collectAsState() // Collect the job details as state
    val job = jobState.value
    val context = LocalContext.current

    // Display job details or a loading message
    if (job != null) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Text(text = job.title, fontWeight = FontWeight.Bold, fontSize = 24.sp)
            Text(text = job.company, fontSize = 18.sp)
            Text(text = job.location, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = job.description, fontSize = 16.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { handleApply(context, job) }) {
                Text("Apply")
            }
        }
    } else {
        // Show a loading message
        Text(
            text = "Loading job details...",
            modifier = Modifier.fillMaxSize(),
            fontSize = 16.sp
        )
    }
}

// Function to handle job application
private fun handleApply(context: android.content.Context, job: JobData) {
    Toast.makeText(context, "Applied for ${job.title}", Toast.LENGTH_SHORT).show()
}





