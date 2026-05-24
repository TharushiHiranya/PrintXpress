package com.hiranya.printxpress.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hiranya.printxpress.data.entity.SavedDesign

@Dao
interface SavedDesignDao {
    @Insert
    suspend fun insert(design: SavedDesign): Long

    @Delete
    suspend fun delete(design: SavedDesign)

    @Query("SELECT * FROM saved_designs WHERE userId = :userId ORDER BY uploadedAt DESC")
    suspend fun getByUser(userId: Long): List<SavedDesign>
}
