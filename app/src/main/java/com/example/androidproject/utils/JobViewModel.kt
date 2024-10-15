package com.example.androidproject.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
    fun deleteJob(jobId: String, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val documentRef = firestore.collection("jobs").document(jobId)
            try {
                documentRef.delete()
                    .addOnSuccessListener {
                        Toast.makeText(context, "Job deleted successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Error deleting job: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
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
    fun getJobDetails(jobId: String) {
        val documentRef = firestore.collection("jobs").document(jobId)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                documentRef.get().addOnSuccessListener { document ->
                    if (document != null) {
                        val jobData = document.toObject(JobData::class.java)
                        _jobDetails.value = jobData
                        Log.d("JobViewModel", "Job data fetched: ${jobData?.title}")
                    } else {
                        Log.d("JobViewModel", "No such document with jobId: $jobId")
                        _jobDetails.value = null
                    }
                }.addOnFailureListener { exception ->
                    Log.d("JobViewModel", "Error fetching job with jobId: $jobId", exception)
                    _jobDetails.value = null
                }
            } catch (e: Exception) {
                Log.d("JobViewModel", "Error fetching job: ${e.message}")
                _jobDetails.value = null
            }
        }
    }
}
