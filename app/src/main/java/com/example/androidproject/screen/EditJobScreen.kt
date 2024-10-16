package com.example.androidproject.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.androidproject.utils.JobData
import com.example.androidproject.utils.JobViewModel
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditJobScreen(
    navController: NavController,
    jobID: String,
    jobViewModel: JobViewModel = viewModel(),
    db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    val context = LocalContext.current
    val jobData = remember { mutableStateOf<JobData?>(null) }
    val isLoading = remember { mutableStateOf(true) }

    // Fetch the job details
    LaunchedEffect(jobID) {
        jobViewModel.getJobById(jobID, { data ->
            jobData.value = data
            isLoading.value = false
        }, context)
    }

    val title = remember { mutableStateOf("") }
    val company = remember { mutableStateOf("") }
    val location = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }

    // Pre-fill form with job data once loaded
    LaunchedEffect(jobData.value) {
        jobData.value?.let { job ->
            title.value = job.title
            company.value = job.company
            location.value = job.location
            description.value = job.description
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Job") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            jobData.value?.let {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    TextField(
                        value = title.value,
                        onValueChange = { title.value = it },
                        label = { Text("Job Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = company.value,
                        onValueChange = { company.value = it },
                        label = { Text("Company Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = location.value,
                        onValueChange = { location.value = it },
                        label = { Text("Location") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = description.value,
                        onValueChange = { description.value = it },
                        label = { Text("Job Description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            // Update job in Firestore
                            val updatedJob = jobData.value?.copy(
                                title = title.value,
                                company = company.value,
                                location = location.value,
                                description = description.value
                            )
                            updatedJob?.let { job ->
                                jobViewModel.updateJob(
                                    job,
                                    onComplete = {
                                        Toast.makeText(context, "Job updated successfully", Toast.LENGTH_SHORT).show()
                                        navController.popBackStack()
                                    },
                                    onFailure = { exception ->
                                        Toast.makeText(context, "Failed to update job: ${exception.message}", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Save Changes", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            } ?: run {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Job not found")
                }
            }
        }
    }
}
