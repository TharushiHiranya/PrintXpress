package com.hiranya.printxpress.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// Delivery address belonging to a user. One can be the default.
@Entity(
    tableName = "addresses",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class Address(
    @PrimaryKey(autoGenerate = true) val addressId: Long = 0,
    val userId: Long,
    val label: String,
    val line1: String,
    val city: String,
    val postalCode: String,
    val isDefault: Boolean = false
)
