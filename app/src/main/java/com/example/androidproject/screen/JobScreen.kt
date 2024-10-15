package com.example.androidproject.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.androidproject.utils.JobData
import com.example.androidproject.utils.JobViewModel

// JobScreen.kt
@Composable
fun JobScreen(navController: NavController, jobViewModel: JobViewModel) {
    var jobs by remember { mutableStateOf<List<JobData>>(emptyList()) }
    val context = LocalContext.current

    // Fetch jobs from Firebase
    LaunchedEffect(Unit) {
        jobViewModel.getAllJobs(onResult = { fetchedJobs ->
            jobs = fetchedJobs
        }, context = context)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
        BasicTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            decorationBox = { innerTextField ->
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color.LightGray, MaterialTheme.shapes.small)
                        .padding(10.dp)
                ) {
                    if (searchQuery.text.isEmpty()) {
                        Text("Search Jobs", color = Color.Gray)
                    }
                    innerTextField()
                }
            }
        )

        LazyColumn {
            items(jobs.size) { index ->
                JobItem(job = jobs[index], onClick = {
                    navController.navigate("jobDetails/${jobs[index].jobID}") // Assuming JobData has an 'id' property
                })
                Divider(color = Color.LightGray, thickness = 1.dp)
            }
        }
    }
}

@Composable
fun JobItem(job: JobData, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onClick) // Make job clickable
    ) {
        Text(text = job.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(text = job.company, color = Color.Gray, fontSize = 14.sp)
        Text(text = job.location, color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = job.description, fontSize = 14.sp)
    }

}
@Composable
fun JobListScreen(navController: NavController, jobs: List<JobData>) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Display a list of jobs
        jobs.forEach { job ->
            JobCard(job = job, onClick = {
                // Navigate to JobDetailsScreen with the job ID when the card is clicked
                navController.navigate("jobDetails/${job.jobID}")
            })
        }
    }
}

@Composable
fun JobCard(job: JobData, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() } // Make the card clickable
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = job.title, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = job.company, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = job.location, fontSize = 14.sp)
        }
    }
}
