package com.hiranya.printxpress.viewmodel

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hiranya.printxpress.data.PrintXpressDatabase
import com.hiranya.printxpress.data.entity.Order
import com.hiranya.printxpress.data.entity.OrderItem
import com.hiranya.printxpress.data.repository.OrderRepository
import com.hiranya.printxpress.data.util.SessionManager
import kotlinx.coroutines.launch

class OrderViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: OrderRepository

    private val _orders = mutableStateOf<List<Order>>(emptyList())
    val orders: State<List<Order>> = _orders

    private val _selectedOrder = mutableStateOf<Order?>(null)
    val selectedOrder: State<Order?> = _selectedOrder

    private val _orderItems = mutableStateOf<List<OrderItem>>(emptyList())
    val orderItems: State<List<OrderItem>> = _orderItems

    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val error: MutableState<String?> = mutableStateOf(null)

    init {
        val db = PrintXpressDatabase.getDatabase(application)
        repo = OrderRepository(db.orderDao(), db.orderItemDao(), db.notificationDao(), db.productDao())
    }

    fun loadOrders() {
        val userId = SessionManager.getUserId() ?: return
        viewModelScope.launch {
            _orders.value = repo.getOrdersByUser(userId)
        }
    }

    fun loadOrder(orderId: Long) {
        viewModelScope.launch {
            _selectedOrder.value = repo.getOrderById(orderId)
            _orderItems.value = repo.getItemsForOrder(orderId)
        }
    }

    fun placeOrder(
        productId: Long,
        quantity: Int,
        size: String,
        material: String,
        designPath: String?,
        customText: String?,
        fulfilment: String,
        addressId: Long?,
        onSuccess: (orderId: Long) -> Unit
    ) {
        val userId = SessionManager.getUserId() ?: return
        viewModelScope.launch {
            isLoading.value = true
            error.value = null
            val orderId = repo.placeOrder(
                userId = userId,
                productId = productId,
                quantity = quantity,
                size = size,
                material = material,
                designPath = designPath,
                customText = customText,
                fulfilment = fulfilment,
                addressId = addressId
            )
            isLoading.value = false
            onSuccess(orderId)
        }
    }

    fun cancelOrder(orderId: Long) {
        viewModelScope.launch {
            repo.cancelOrder(orderId)
            loadOrders()
            // Reload the selected order if it was the cancelled one.
            if (_selectedOrder.value?.orderId == orderId) {
                _selectedOrder.value = repo.getOrderById(orderId)
            }
        }
    }
}
