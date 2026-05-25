package com.hiranya.printxpress.viewmodel

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hiranya.printxpress.data.PrintXpressDatabase
import com.hiranya.printxpress.data.entity.Address
import com.hiranya.printxpress.data.entity.Order
import com.hiranya.printxpress.data.entity.OrderItem
import com.hiranya.printxpress.data.entity.Promotion
import com.hiranya.printxpress.data.repository.AddressRepository
import com.hiranya.printxpress.data.repository.OrderRepository
import com.hiranya.printxpress.data.repository.ProductRepository
import com.hiranya.printxpress.data.repository.PromotionRepository
import com.hiranya.printxpress.data.util.SessionManager
import kotlinx.coroutines.launch

class OrderViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: OrderRepository
    private val addressRepo: AddressRepository
    private val promotionRepo: PromotionRepository
    private val productRepo: ProductRepository

    private val _orders = mutableStateOf<List<Order>>(emptyList())
    val orders: State<List<Order>> = _orders

    private val _selectedOrder = mutableStateOf<Order?>(null)
    val selectedOrder: State<Order?> = _selectedOrder

    private val _orderItems = mutableStateOf<List<OrderItem>>(emptyList())
    val orderItems: State<List<OrderItem>> = _orderItems

    private val _orderAddress = mutableStateOf<Address?>(null)
    val orderAddress: State<Address?> = _orderAddress

    private val _userAddresses = mutableStateOf<List<Address>>(emptyList())
    val userAddresses: State<List<Address>> = _userAddresses

    private val _selectedAddressId = mutableStateOf<Long?>(null)
    val selectedAddressId: State<Long?> = _selectedAddressId

    private val _appliedPromotion = mutableStateOf<Promotion?>(null)
    val appliedPromotion: State<Promotion?> = _appliedPromotion

    private val _promoMessage = mutableStateOf<String?>(null)
    val promoMessage: State<String?> = _promoMessage

    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val error: MutableState<String?> = mutableStateOf(null)

    init {
        val db = PrintXpressDatabase.getDatabase(application)
        repo = OrderRepository(db.orderDao(), db.orderItemDao(), db.notificationDao(), db.productDao())
        addressRepo = AddressRepository(db.addressDao())
        promotionRepo = PromotionRepository(db.promotionDao())
        productRepo = ProductRepository(db.categoryDao(), db.productDao())
        loadAddresses()
    }

    fun applyPromoCode(code: String, productId: Long, subtotal: Double) {
        viewModelScope.launch {
            val promo = promotionRepo.getByCode(code)
            if (promo == null) {
                _promoMessage.value = "Invalid promo code"
                _appliedPromotion.value = null
                return@launch
            }

            // Check if product specific
            if (promo.productId != null && promo.productId != productId) {
                _promoMessage.value = "Promo code cannot be applied to this product"
                _appliedPromotion.value = null
                return@launch
            }

            // Check if category specific
            if (promo.categoryId != null) {
                val product = productRepo.getProductById(productId)
                if (product?.categoryId != promo.categoryId) {
                    _promoMessage.value = "Promo code cannot be applied to this product category"
                    _appliedPromotion.value = null
                    return@launch
                }
            }

            // Check threshold
            if (promo.minOrderValue != null && subtotal < promo.minOrderValue) {
                if (promo.isFreeDelivery) {
                    _promoMessage.value = "Order total must be LKR ${promo.minOrderValue.toInt()} for free delivery"
                } else {
                    _promoMessage.value = "Order total must be at least LKR ${promo.minOrderValue.toInt()} to use this code"
                }
                _appliedPromotion.value = null
                return@launch
            }

            _appliedPromotion.value = promo
            _promoMessage.value = "Promo code applied: ${promo.title}"
        }
    }

    fun clearPromoMessage() {
        _promoMessage.value = null
    }

    fun loadAddresses() {
        val userId = SessionManager.getUserId() ?: return
        viewModelScope.launch {
            val addresses = addressRepo.getByUser(userId)
            _userAddresses.value = addresses
            // Default to the first address if available.
            if (_selectedAddressId.value == null && addresses.isNotEmpty()) {
                _selectedAddressId.value = addresses.first().addressId
            }
        }
    }

    fun selectAddress(addressId: Long) {
        _selectedAddressId.value = addressId
    }

    fun loadOrders() {
        val userId = SessionManager.getUserId() ?: return
        viewModelScope.launch {
            _orders.value = repo.getOrdersByUser(userId)
        }
    }

    fun loadOrder(orderId: Long) {
        viewModelScope.launch {
            val order = repo.getOrderById(orderId)
            _selectedOrder.value = order
            _orderItems.value = repo.getItemsForOrder(orderId)
            
            if (order?.deliveryAddressId != null) {
                _orderAddress.value = addressRepo.getById(order.deliveryAddressId)
            } else {
                _orderAddress.value = null
            }
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
