package com.hiranya.printxpress.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// A design file that the user saved to reuse on future orders.
@Entity(
    tableName = "saved_designs",
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
data class SavedDesign(
    @PrimaryKey(autoGenerate = true) val designId: Long = 0,
    val userId: Long,
    val title: String,
    val filePath: String,
    val uploadedAt: Long
)
