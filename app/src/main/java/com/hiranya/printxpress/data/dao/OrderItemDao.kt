package com.hiranya.printxpress.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hiranya.printxpress.data.entity.OrderItem

@Dao
interface OrderItemDao {
    @Insert
    suspend fun insert(item: OrderItem): Long

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    suspend fun getByOrder(orderId: Long): List<OrderItem>
}
