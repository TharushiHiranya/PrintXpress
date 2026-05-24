package com.hiranya.printxpress.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hiranya.printxpress.data.entity.Product

@Dao
interface ProductDao {
    @Insert
    suspend fun insert(product: Product): Long

    @Query("SELECT * FROM products WHERE categoryId = :categoryId ORDER BY name ASC")
    suspend fun getByCategory(categoryId: Long): List<Product>

    @Query("SELECT * FROM products WHERE productId = :productId LIMIT 1")
    suspend fun getById(productId: Long): Product?

    @Query("SELECT * FROM products ORDER BY name ASC")
    suspend fun getAll(): List<Product>
}
