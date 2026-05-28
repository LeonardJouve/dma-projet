package com.example.borne.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.borne.database.models.AttendanceEvent
import com.example.borne.database.models.User

@Dao
interface AttendanceDao {
    @Insert()
    suspend fun insert(event: AttendanceEvent)

    @Insert()
    suspend fun insert(user: User)

    @Query("SELECT * FROM AttendanceEvent WHERE userId = :userId ORDER BY time DESC LIMIT 1")
    fun getUserLastEvent(userId: Long) : LiveData<AttendanceEvent?>
}