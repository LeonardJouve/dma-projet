package com.example.borne.database

import androidx.lifecycle.LiveData
import com.example.borne.database.models.AttendanceEvent
import com.example.borne.database.models.EventType
import com.example.borne.database.models.User

class AttendanceRepository(private val dao: AttendanceDao) {
    suspend fun badge(user: User) {
        dao.insert(AttendanceEvent(
            userId = user.id!!,
            type = when (getUserLastEvent(user).value?.type) {
                EventType.IN -> EventType.OUT
                else -> EventType.IN
            },
            time = System.currentTimeMillis(),
        ))
    }

    suspend fun insertUser(user: User): Long {
        return dao.insert(user)
    }

    fun getUserLastEvent(user: User): LiveData<AttendanceEvent?> {
        return dao.getUserLastEvent(user.id!!)
    }

    fun getUsers(): LiveData<List<User>> {
        return dao.getUsers()
    }

    fun getUserEventHistory(user: User): LiveData<List<AttendanceEvent>> {
        return dao.getUserEventHistory(user.id!!)
    }
}
