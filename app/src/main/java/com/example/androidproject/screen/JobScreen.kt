package com.example.androidproject.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
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

@Composable
fun JobScreen(navController: NavController, jobViewModel: JobViewModel) {
    // Job list state
    var jobs by remember { mutableStateOf<List<JobData>>(emptyList()) }
    var context = LocalContext.current

    // Fetch jobs from Firebase
    LaunchedEffect(Unit) {
        jobViewModel.getAllJobs(onResult = { fetchedJobs ->
            jobs = fetchedJobs
        }, context = context)
    }

    // Column for search bar and list of jobs
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Search bar
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

        // LazyColumn for job listings
        LazyColumn {
            items(jobs.size) { index ->
                JobItem(job = jobs[index])
                Divider(color = Color.LightGray, thickness = 1.dp)
            }
        }
    }
}

@Composable
fun JobItem(job: JobData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = job.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(text = job.company, color = Color.Gray, fontSize = 14.sp)
        Text(text = job.location, color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = job.description, fontSize = 14.sp)
    }
}
