package com.hiranya.printxpress.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hiranya.printxpress.data.entity.Address

@Dao
interface AddressDao {
    @Insert
    suspend fun insert(address: Address): Long

    @Update
    suspend fun update(address: Address)

    @Delete
    suspend fun delete(address: Address)

    @Query("SELECT * FROM addresses WHERE userId = :userId ORDER BY isDefault DESC")
    suspend fun getByUser(userId: Long): List<Address>

    // Clear all defaults for the user, then mark the chosen address as default.
    @Query("UPDATE addresses SET isDefault = 0 WHERE userId = :userId")
    suspend fun clearDefaults(userId: Long)

    @Query("UPDATE addresses SET isDefault = 1 WHERE addressId = :addressId")
    suspend fun markDefault(addressId: Long)

    @Query("SELECT * FROM addresses WHERE addressId = :addressId LIMIT 1")
    suspend fun getById(addressId: Long): Address?
}
