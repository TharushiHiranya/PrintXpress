package com.hiranya.printxpress.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// One row per registered account. Email or phone is used as the login id.
@Entity(
    tableName = "users",
    indices = [
        Index(value = ["email"], unique = true),
        Index(value = ["phone"], unique = true)
    ]
)
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Long = 0,
    val fullName: String,
    val email: String?,
    val phone: String?,
    val passwordHash: String,
    val createdAt: Long
)
