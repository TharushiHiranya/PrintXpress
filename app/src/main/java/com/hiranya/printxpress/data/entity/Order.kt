package com.hiranya.printxpress.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// One placed order. The total is stored so a past order keeps its price even if the catalog changes.
@Entity(
    tableName = "orders",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Address::class,
            parentColumns = ["addressId"],
            childColumns = ["deliveryAddressId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Promotion::class,
            parentColumns = ["promoId"],
            childColumns = ["promoId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("userId"), Index("deliveryAddressId"), Index("promoId")]
)
data class Order(
    @PrimaryKey(autoGenerate = true) val orderId: Long = 0,
    val userId: Long,
    val orderDate: Long,
    val status: String,
    val fulfilment: String,
    val deliveryAddressId: Long?,
    val promoId: Long?,
    val totalAmount: Double,
    val notes: String?
)
