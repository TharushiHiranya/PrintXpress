package com.hiranya.printxpress.data.repository

import com.hiranya.printxpress.data.dao.SavedDesignDao
import com.hiranya.printxpress.data.entity.SavedDesign

class SavedDesignRepository(private val savedDesignDao: SavedDesignDao) {
    suspend fun getSavedDesignsByUser(userId: Long): List<SavedDesign> {
        return savedDesignDao.getByUser(userId)
    }

    suspend fun saveDesign(design: SavedDesign): Long {
        return savedDesignDao.insert(design)
    }

    suspend fun deleteDesign(design: SavedDesign) {
        savedDesignDao.delete(design)
    }
}
