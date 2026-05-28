package com.example.borne.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.borne.database.models.AttendanceEvent
import com.example.borne.database.models.User

@Database(entities = [User::class, AttendanceEvent::class], version = 1, exportSchema = true)
abstract class AttendanceDatabase : RoomDatabase() {
    abstract fun attendanceDao() : AttendanceDao

    companion object {

        @Volatile
        private var INSTANCE : AttendanceDatabase? = null

        fun getDatabase(context: Context) : AttendanceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AttendanceDatabase::class.java,
                    "database.db")
                    .fallbackToDestructiveMigration(true)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }

}