package com.hiranya.printxpress.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Seasonal offer or discount code. Active when the current time falls between validFrom and validTo.
@Entity(tableName = "promotions")
data class Promotion(
    @PrimaryKey(autoGenerate = true) val promoId: Long = 0,
    val title: String,
    val description: String,
    val code: String,
    val validFrom: Long,
    val validTo: Long,
    val discountPercent: Int? = null,
    val discountAmount: Double? = null,
    val isFreeDelivery: Boolean = false,
    val minOrderValue: Double? = null,
    val productId: Long? = null,
    val categoryId: Long? = null
)
