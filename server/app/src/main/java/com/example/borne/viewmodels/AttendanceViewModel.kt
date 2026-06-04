package com.example.borne.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.borne.database.AttendanceRepository
import com.example.borne.database.models.AttendanceEvent
import com.example.borne.database.models.User
import com.example.borne.database.models.UserWithLastEvent

class AttendanceViewModel(private val repository: AttendanceRepository) : ViewModel() {

    suspend fun badge(user: User) {
        return repository.badge(user)
    }

    fun getUsers(): LiveData<List<User>> {
        return repository.getUsers()
    }

    fun getUsersWithLastEvent(): LiveData<List<UserWithLastEvent>> {
        return repository.getUsersWithLastEvent()
    }

    fun getUserEventHistory(user: User): LiveData<List<AttendanceEvent>> {
        return repository.getUserEventHistory(user)
    }

    fun getUserLastEvent(user: User): LiveData<AttendanceEvent?> {
        return repository.getUserLastEvent(user)
    }

    suspend fun insertUser(user: User): Long {
        return repository.insertUser(user)
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