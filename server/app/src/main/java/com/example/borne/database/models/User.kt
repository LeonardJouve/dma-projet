package com.example.borne.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class User(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var name: String,
    var secret: String
) : java.io.Serializable
