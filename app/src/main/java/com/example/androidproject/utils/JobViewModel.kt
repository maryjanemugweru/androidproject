package com.example.androidproject.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class JobViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    // MutableStateFlow to hold the job details
    private val _jobDetails = MutableStateFlow<JobData?>(null)
    val jobDetails: StateFlow<JobData?> get() = _jobDetails

    // Function to save or update job data
    fun saveJob(jobData: JobData, context: Context) = CoroutineScope(Dispatchers.IO).launch {
        val fireStoreRef = firestore.collection("jobs").document(jobData.jobID)

        try {
            fireStoreRef.set(jobData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully updated job data", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, exception.message ?: "Failed to update job data", Toast.LENGTH_SHORT).show()
                }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    // Function to delete a job
    fun deleteJob(
        jobID: String,
        navController: NavController,
        context: Context
    ) = CoroutineScope(Dispatchers.IO).launch {
        val fireStoreRef = Firebase.firestore
            .collection("jobs")
            .document(jobID)

        try {
            fireStoreRef.delete()
                .addOnSuccessListener {
                    // Show a success message on the UI thread
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context, "Successfully deleted job", Toast.LENGTH_SHORT).show()
                        navController.popBackStack() // Navigate back to the previous screen
                    }
                }
        } catch (e: Exception) {
            // Show error message on the UI thread
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getAllJobs(onResult: (List<JobData>) -> Unit, context: Context) = CoroutineScope(Dispatchers.IO).launch {
        val fireStoreRef = firestore.collection("jobs")

        try {
            fireStoreRef.get().addOnSuccessListener { result: QuerySnapshot ->
                val jobs = result.documents.map { document ->
                    document.toObject<JobData>()!!
                }
                onResult(jobs)
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to fetch jobs", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    // Updated getJobDetails to use StateFlow
    fun getJobById(
        jobID: String,
        onResult: (JobData?) -> Unit,
        context: Context
    ) {
        // Launch the coroutine in the IO dispatcher
        CoroutineScope(Dispatchers.IO).launch {
            val fireStoreRef = FirebaseFirestore.getInstance()
            val documentRef = fireStoreRef.collection("jobs").document(jobID)

            try {
                // Get the document snapshot
                val documentSnapshot = documentRef.get().await() // Ensure you have the necessary dependencies for await()
                val jobData = documentSnapshot.toObject<JobData>()

                // Post the result back to the main thread
                withContext(Dispatchers.Main) {
                    onResult(jobData)
                }
            } catch (e: Exception) {
                // Show toast on the main thread
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message ?: "Error fetching job", Toast.LENGTH_SHORT).show()
                    onResult(null)
                }
            }
        }
    }

    fun submitApplication(applicationData: ApplicationData, context: Context, onComplete: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val applicationRef = firestore.collection("applications").document(applicationData.applicationId)

            try {
                applicationRef.set(applicationData)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Application submitted successfully", Toast.LENGTH_SHORT).show()
                        onComplete() // Trigger completion callback (e.g., navigation)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Error submitting application: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getUserApplications(userId: String, onComplete: (List<ApplicationData>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("applications")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val applications = result.documents.mapNotNull { it.toObject(ApplicationData::class.java) }
                onComplete(applications)
            }
            .addOnFailureListener {
                onComplete(emptyList()) // Return an empty list on failure
            }
    }

    fun getAllApplications(onComplete: (List<ApplicationData>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("applications")
            .get()
            .addOnSuccessListener { result ->
                val applications = result.documents.mapNotNull { it.toObject(ApplicationData::class.java) }
                onComplete(applications)
            }
            .addOnFailureListener {
                onComplete(emptyList()) // Return an empty list on failure
            }
    }

    fun updateApplicationStatus(applicationId: String, status: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("applications").document(applicationId)
            .update("status", status)
            .addOnSuccessListener {
                // Notify the admin about success (optional: show a Toast or Snackbar)
            }
            .addOnFailureListener {
                // Handle failure (optional: show a Toast or Snackbar)
            }
    }

    fun updateJob(jobData: JobData, onComplete: () -> Unit, onFailure: (Exception) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("jobs").document(jobData.jobID)
            .set(jobData)
            .addOnSuccessListener { onComplete() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }


}
