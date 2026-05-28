package com.example.borne.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.borne.database.AttendanceRepository
import com.example.borne.database.models.User

class AttendanceViewModel(private val repository: AttendanceRepository) : ViewModel() {
    suspend fun insertUser(user: User) {
        repository.insertUser(user)
    }
}

class AttendanceViewModelFactory(private val repository: AttendanceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AttendanceViewModel::class.java)) {
            return AttendanceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}