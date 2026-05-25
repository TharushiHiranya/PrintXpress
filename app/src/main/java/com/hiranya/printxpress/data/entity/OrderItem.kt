package com.hiranya.printxpress.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// A single line inside an order: one product with its chosen size, material, quantity, and design.
@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(
            entity = Order::class,
            parentColumns = ["orderId"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Product::class,
            parentColumns = ["productId"],
            childColumns = ["productId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("orderId"), Index("productId")]
)
data class OrderItem(
    @PrimaryKey(autoGenerate = true) val orderItemId: Long = 0,
    val orderId: Long,
    val productId: Long,
    val productName: String,
    val productImageRef: String,
    val quantity: Int,
    val paperType: String,
    val size: String,
    val unitPrice: Double,
    val designPath: String?,
    val customText: String?
)
