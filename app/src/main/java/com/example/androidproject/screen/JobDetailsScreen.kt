package com.example.androidproject.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.androidproject.utils.JobData
import com.example.androidproject.utils.JobViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun JobDetailsScreen(
    navController: NavController,
    jobID: String,
    jobViewModel: JobViewModel = viewModel(),
    db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    currentUser: FirebaseAuth? = FirebaseAuth.getInstance()
) {
    val context = LocalContext.current
    val jobData = remember { mutableStateOf<JobData?>(null) }
    val isLoading = remember { mutableStateOf(true) }
    val userRole = remember { mutableStateOf<String?>(null) }
    val isLoadingRole = remember { mutableStateOf(true) }

    // Fetch the job details
    LaunchedEffect(jobID) {
        jobViewModel.getJobById(jobID, { data ->
            jobData.value = data
            isLoading.value = false
        }, context)
    }

    // Fetch the user role
    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { uid ->
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    userRole.value = document.getString("role")
                    isLoadingRole.value = false
                }
                .addOnFailureListener {
                    userRole.value = null
                    isLoadingRole.value = false
                }
        }
    }

    Scaffold(
        topBar = {

        }
    ) { paddingValues ->
        if (isLoading.value || isLoadingRole.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            jobData.value?.let { job ->
                // Display job details
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(text = job.title, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 8.dp))
                    Text(text = job.company, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 4.dp))
                    Text(text = job.location, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 16.dp))

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(text = "Description", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 8.dp))
                    Text(text = job.description, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 16.dp))

                    Spacer(modifier = Modifier.height(16.dp))

                    // Apply button logic
                    Button(
                        onClick = {
                            navController.navigate("jobApplication/{${job.jobID}")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Apply", color = MaterialTheme.colorScheme.onPrimary)
                    }

                    // Display Edit and Delete buttons if the user is an admin
                    if (userRole.value == "admin") {
                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    // Navigate to the EditJobScreen
                                    navController.navigate("editJobs_screen/$jobID")
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                            ) {
                                Text("Edit", color = MaterialTheme.colorScheme.onSecondary)
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Button(
                                onClick = {
                                    jobViewModel.deleteJob(jobID, navController, context)
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text("Delete", color = MaterialTheme.colorScheme.onError)
                            }
                        }
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