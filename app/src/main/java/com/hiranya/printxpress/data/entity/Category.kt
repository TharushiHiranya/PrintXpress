package com.hiranya.printxpress.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Top-level product group, for example business cards or t-shirts. Seeded on first run.
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val categoryId: Long = 0,
    val name: String,
    val description: String,
    val iconRef: String
)
