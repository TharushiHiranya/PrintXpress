package com.hiranya.printxpress.data.repository

import com.hiranya.printxpress.data.dao.AddressDao
import com.hiranya.printxpress.data.entity.Address

class AddressRepository(private val addressDao: AddressDao) {
    suspend fun getByUser(userId: Long): List<Address> = addressDao.getByUser(userId)

    suspend fun add(address: Address): Long = addressDao.insert(address)

    suspend fun update(address: Address) = addressDao.update(address)

    suspend fun delete(address: Address) = addressDao.delete(address)

    suspend fun getById(addressId: Long): Address? = addressDao.getById(addressId)

    // Clear all defaults for the user then set the chosen one.
    suspend fun setDefault(userId: Long, addressId: Long) {
        addressDao.clearDefaults(userId)
        addressDao.markDefault(addressId)
    }
}
