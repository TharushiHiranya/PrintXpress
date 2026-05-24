package com.hiranya.printxpress.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// A printable product inside a category. Size and material options are comma-separated strings.
@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("categoryId")]
)
data class Product(
    @PrimaryKey(autoGenerate = true) val productId: Long = 0,
    val categoryId: Long,
    val name: String,
    val description: String,
    val basePrice: Double,
    val material: String,
    val imageRef: String,
    val sizeOptions: String,
    val materialOptions: String
)
