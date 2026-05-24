package com.hiranya.printxpress.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hiranya.printxpress.data.PrintXpressDatabase
import com.hiranya.printxpress.data.entity.Category
import com.hiranya.printxpress.data.entity.Promotion
import com.hiranya.printxpress.data.repository.NotificationRepository
import com.hiranya.printxpress.data.repository.ProductRepository
import com.hiranya.printxpress.data.repository.PromotionRepository
import com.hiranya.printxpress.data.util.SessionManager
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val productRepo: ProductRepository
    private val promotionRepo: PromotionRepository
    private val notificationRepo: NotificationRepository

    private val _categories = mutableStateOf<List<Category>>(emptyList())
    val categories: State<List<Category>> = _categories

    private val _activePromotions = mutableStateOf<List<Promotion>>(emptyList())
    val activePromotions: State<List<Promotion>> = _activePromotions

    private val _unreadCount = mutableStateOf(0)
    val unreadCount: State<Int> = _unreadCount

    init {
        val db = PrintXpressDatabase.getDatabase(application)
        productRepo = ProductRepository(db.categoryDao(), db.productDao())
        promotionRepo = PromotionRepository(db.promotionDao())
        notificationRepo = NotificationRepository(db.notificationDao())
        load()
    }

    fun load() {
        viewModelScope.launch {
            _categories.value = productRepo.getCategories()
            _activePromotions.value = promotionRepo.getActive()
            val userId = SessionManager.getUserId()
            if (userId != null) {
                _unreadCount.value = notificationRepo.getByUser(userId).count { !it.isRead }
            }
        }
    }
}
