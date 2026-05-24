package com.hiranya.printxpress.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hiranya.printxpress.data.entity.Category

@Dao
interface CategoryDao {
    @Insert
    suspend fun insert(category: Category): Long

    @Query("SELECT * FROM categories ORDER BY name ASC")
    suspend fun getAll(): List<Category>
}
