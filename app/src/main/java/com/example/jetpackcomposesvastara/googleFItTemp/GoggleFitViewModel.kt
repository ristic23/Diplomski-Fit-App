package com.example.jetpackcomposesvastara.googleFItTemp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NotificationsViewModel(private val repository: MiBandRepository) : ViewModel() {

}

class GoogleFitViewModelFactory(private val repository: MiBandRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}