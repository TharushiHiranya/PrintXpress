package com.hiranya.printxpress.data.repository

import com.hiranya.printxpress.data.dao.PromotionDao
import com.hiranya.printxpress.data.entity.Promotion

class PromotionRepository(private val promotionDao: PromotionDao) {
    suspend fun getActive(): List<Promotion> = promotionDao.getActive(System.currentTimeMillis())
    suspend fun getByCode(code: String): Promotion? = promotionDao.getByCode(code, System.currentTimeMillis())
}
