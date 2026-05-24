package com.hiranya.printxpress.viewmodel

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hiranya.printxpress.data.PrintXpressDatabase
import com.hiranya.printxpress.data.entity.Product
import com.hiranya.printxpress.data.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: ProductRepository

    private val _products = mutableStateOf<List<Product>>(emptyList())
    val products: State<List<Product>> = _products

    private val _allProducts = mutableStateOf<List<Product>>(emptyList())

    private val _selectedProduct = mutableStateOf<Product?>(null)
    val selectedProduct: State<Product?> = _selectedProduct

    val searchQuery: MutableState<String> = mutableStateOf("")

    init {
        val db = PrintXpressDatabase.getDatabase(application)
        repo = ProductRepository(db.categoryDao(), db.productDao())
    }

    fun loadProductsByCategory(categoryId: Long) {
        viewModelScope.launch {
            val all = repo.getProductsByCategory(categoryId)
            _allProducts.value = all
            applyFilter()
        }
    }

    // Re-filter whenever the search query changes.
    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
        applyFilter()
    }

    private fun applyFilter() {
        val q = searchQuery.value.trim()
        _products.value = if (q.isEmpty()) {
            _allProducts.value
        } else {
            _allProducts.value.filter { it.name.contains(q, ignoreCase = true) }
        }
    }

    fun loadProduct(productId: Long) {
        viewModelScope.launch {
            _selectedProduct.value = repo.getProductById(productId)
        }
    }
}
