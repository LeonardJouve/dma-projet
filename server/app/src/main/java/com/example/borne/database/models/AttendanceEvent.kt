package com.example.borne.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class AttendanceEvent(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var userId: Long,
    var type: EventType,
    var time: Long)