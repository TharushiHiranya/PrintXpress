package com.hiranya.printxpress.data.repository

import com.hiranya.printxpress.data.dao.CategoryDao
import com.hiranya.printxpress.data.dao.ProductDao
import com.hiranya.printxpress.data.entity.Category
import com.hiranya.printxpress.data.entity.Product

class ProductRepository(
    private val categoryDao: CategoryDao,
    private val productDao: ProductDao
) {
    suspend fun getCategories(): List<Category> = categoryDao.getAll()

    suspend fun getProductsByCategory(categoryId: Long): List<Product> =
        if (categoryId == 0L) productDao.getAll() else productDao.getByCategory(categoryId)

    suspend fun getAllProducts(): List<Product> = productDao.getAll()

    suspend fun getProductById(productId: Long): Product? = productDao.getById(productId)
}
