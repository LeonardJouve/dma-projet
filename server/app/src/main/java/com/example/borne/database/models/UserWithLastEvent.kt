package com.example.borne.database.models

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithLastEvent (
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val attendanceEvent: AttendanceEvent?
)
