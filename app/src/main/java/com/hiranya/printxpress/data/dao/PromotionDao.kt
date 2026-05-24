package com.hiranya.printxpress.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hiranya.printxpress.data.entity.Promotion

@Dao
interface PromotionDao {
    @Insert
    suspend fun insert(promotion: Promotion): Long

    // Return only offers that are currently active based on the current time.
    @Query("SELECT * FROM promotions WHERE validFrom <= :now AND validTo >= :now")
    suspend fun getActive(now: Long): List<Promotion>
}
