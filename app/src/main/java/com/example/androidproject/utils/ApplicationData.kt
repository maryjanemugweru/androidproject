package com.example.androidproject.utils

data class ApplicationData(
    val applicationId: String = "",
    val jobId: String = "",
    val userId: String = "",
    val applicantName: String = "",
    val applicantEmail: String = "",
    val coverLetter: String = "",
    val resumeUrl: String = "",
    val status: String = "Pending",
    val applicationDate: Long = System.currentTimeMillis()
)
