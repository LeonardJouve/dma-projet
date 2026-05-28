package com.example.borne

import android.app.Application
import com.example.borne.database.AttendanceDatabase
import com.example.borne.database.AttendanceRepository

class App: Application() {
    val repository by lazy {
        val database = AttendanceDatabase.getDatabase(this)
        AttendanceRepository(database.attendanceDao())
    }
}