package com.example.androidproject.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.androidproject.utils.JobData
import com.example.androidproject.utils.JobViewModel

@Composable
fun JobPostScreen(navController: NavController, jobViewModel: JobViewModel) {
    // Form state variables
    var jobTitle by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    var context = LocalContext.current

    // Handle form submission
    fun postJob() {
        if (jobTitle.isNotBlank() && company.isNotBlank() && location.isNotBlank() && description.isNotBlank()) {
            isSubmitting = true
            val jobData = JobData(
                jobID = System.currentTimeMillis().toString(), // Unique ID for the job
                title = jobTitle,
                company = company,
                location = location,
                description = description
            )

            jobViewModel.saveJob(jobData, context = context)
            isSubmitting = false
            navController.popBackStack() // Navigate back after successful posting
        } else {
            Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
        }
    }

    // UI layout for the job post screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Post a Job", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        // Input fields for job details
        BasicTextField(
            value = jobTitle,
            onValueChange = { jobTitle = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .background(Color.LightGray, MaterialTheme.shapes.small)
                .padding(8.dp),
            decorationBox = { innerTextField ->
                if (jobTitle.isEmpty()) Text("Job Title", color = Color.Gray)
                innerTextField()
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        BasicTextField(
            value = company,
            onValueChange = { company = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .background(Color.LightGray, MaterialTheme.shapes.small)
                .padding(8.dp),
            decorationBox = { innerTextField ->
                if (company.isEmpty()) Text("Company", color = Color.Gray)
                innerTextField()
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        BasicTextField(
            value = location,
            onValueChange = { location = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .background(Color.LightGray, MaterialTheme.shapes.small)
                .padding(8.dp),
            decorationBox = { innerTextField ->
                if (location.isEmpty()) Text("Location", color = Color.Gray)
                innerTextField()
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        BasicTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .background(Color.LightGray, MaterialTheme.shapes.small)
                .padding(8.dp),
            decorationBox = { innerTextField ->
                if (description.isEmpty()) Text("Job Description", color = Color.Gray)
                innerTextField()
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Submit button
        Button(
            onClick = { postJob() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSubmitting
        ) {
            Text(text = if (isSubmitting) "Submitting..." else "Post Job")
        }
    }
}
