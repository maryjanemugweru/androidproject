package com.example.androidproject.utils

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class DashboardViewModel : ViewModel() {
    // Holds the current screen or tab the user is on
    private var currentScreen by mutableStateOf("home")

    // Updates the current screen when a tab or screen is clicked
    fun updateScreen(newScreen: String) {
        currentScreen = newScreen
    }
}