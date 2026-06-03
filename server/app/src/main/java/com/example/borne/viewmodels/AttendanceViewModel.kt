package com.example.borne.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.borne.UserItem
import com.example.borne.database.AttendanceRepository
import com.example.borne.database.models.User
import java.util.Date

class AttendanceViewModel(private val repository: AttendanceRepository) : ViewModel() {
    // TODO : Valeurs placeholder
    val users = MutableLiveData<MutableList<UserItem>>(
        mutableListOf(
            UserItem("Alice", true, Date()),
            UserItem("Bob", false, Date()),
            UserItem("Charlie", true, Date()),
            UserItem("David", true, Date())
        )
    )

    /**
     * TODO : A priori sera remplacé par la mise à jour de la DB
     *
     * Met à jour l'utilisateur à l'index passé en paramètre
     * - Toggle le statut
     * - Met à jour la date à l'instant actuel
     */
    fun updateUser(index: Int) {
        val list = users.value ?: return
        val item = list[index]
        list[index] = item.copy(status = !item.status, date = Date())
        users.value = list
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