package com.hiranya.printxpress.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Seasonal offer or discount code. Active when the current time falls between validFrom and validTo.
@Entity(tableName = "promotions")
data class Promotion(
    @PrimaryKey(autoGenerate = true) val promoId: Long = 0,
    val title: String,
    val description: String,
    val discountPercent: Int,
    val code: String,
    val validFrom: Long,
    val validTo: Long
)
