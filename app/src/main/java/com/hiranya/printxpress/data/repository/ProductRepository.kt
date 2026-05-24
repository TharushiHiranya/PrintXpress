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
        productDao.getByCategory(categoryId)

    suspend fun getProductById(productId: Long): Product? = productDao.getById(productId)
}
