package com.example.androidproject.utils

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JobViewModel : ViewModel() {
    fun saveJob(
        jobData: JobData,
        context: Context

    ) = CoroutineScope(Dispatchers.IO).launch {

        val fireStoreRef = Firebase.firestore
            .collection("jobs")
            .document(jobData.jobID)

        try {
            fireStoreRef.set(jobData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully posted data", Toast.LENGTH_SHORT).show()

                }
        } catch (e: Exception) {

            Toast.makeText(
                context, e.message,

                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun getAllJobs(
        onResult: (List<JobData>) -> Unit,
        context: Context
    ) = CoroutineScope(Dispatchers.IO).launch {
        val fireStoreRef = Firebase.firestore.collection("jobs")

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

    fun getJobById(
        jobID: String,
        onResult: (JobData?) -> Unit,
        context: Context
    ) = CoroutineScope(Dispatchers.IO).launch {

        val fireStoreRef = FirebaseFirestore.getInstance()
        val documentRef = fireStoreRef
            .collection("jobs")
            .document(jobID)

        try {
            documentRef.get().addOnSuccessListener {
                val jobData = it.toObject<JobData>()
                onResult(jobData)
            }.addOnFailureListener {
                Toast.makeText(context, "Error fetching job", Toast.LENGTH_SHORT).show()
                onResult(null)
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            onResult(null)
        }
    }
}