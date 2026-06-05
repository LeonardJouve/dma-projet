package com.example.borne.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.borne.database.models.AttendanceEvent
import com.example.borne.database.models.User
import com.example.borne.database.models.UserWithLastEvent

@Dao
interface AttendanceDao {
    @Insert()
    suspend fun insert(event: AttendanceEvent)

    @Insert()
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM AttendanceEvent WHERE userId = :userId ORDER BY time DESC LIMIT 1")
    fun getUserLastEvent(userId: Long) : LiveData<AttendanceEvent?>

    @Query("SELECT * FROM AttendanceEvent WHERE userId = :userId ORDER BY time DESC LIMIT 1")
    suspend fun getUserLastEventNotLiveData(userId: Long) : AttendanceEvent?

    @Query("SELECT * FROM AttendanceEvent WHERE userId = :userId ORDER BY time DESC")
    fun getUserEventHistory(userId: Long) : LiveData<List<AttendanceEvent>>

    @Query("SELECT * FROM User")
    fun getUsers() : LiveData<List<User>>

    @Query("SELECT * FROM User WHERE id = :userId LIMIT 1")
    fun getUser(userId: Long): User?

    @Transaction
    @Query("SELECT * FROM User")
    fun getUsersWithLastEvent() : LiveData<List<UserWithLastEvent>>
}