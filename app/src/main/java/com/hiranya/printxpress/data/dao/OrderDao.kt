package com.hiranya.printxpress.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hiranya.printxpress.data.entity.Order

@Dao
interface OrderDao {
    @Insert
    suspend fun insert(order: Order): Long

    @Update
    suspend fun update(order: Order)

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY orderDate DESC")
    suspend fun getByUser(userId: Long): List<Order>

    @Query("SELECT * FROM orders WHERE orderId = :orderId LIMIT 1")
    suspend fun getById(orderId: Long): Order?
}
