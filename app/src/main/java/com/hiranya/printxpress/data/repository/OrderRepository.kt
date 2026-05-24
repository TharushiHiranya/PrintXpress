package com.hiranya.printxpress.data.repository

import com.hiranya.printxpress.data.dao.NotificationDao
import com.hiranya.printxpress.data.dao.OrderDao
import com.hiranya.printxpress.data.dao.OrderItemDao
import com.hiranya.printxpress.data.dao.ProductDao
import com.hiranya.printxpress.data.entity.Notification
import com.hiranya.printxpress.data.entity.Order
import com.hiranya.printxpress.data.entity.OrderItem

class OrderRepository(
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao,
    private val notificationDao: NotificationDao,
    private val productDao: ProductDao
) {
    // Insert the order, its item, and a confirmation notification in one call.
    suspend fun placeOrder(
        userId: Long,
        productId: Long,
        quantity: Int,
        size: String,
        material: String,
        designPath: String?,
        customText: String?,
        fulfilment: String,
        addressId: Long?
    ): Long {
        val product = productDao.getById(productId)
        val unitPrice = product?.basePrice ?: 0.0
        val deliveryFee = if (fulfilment == "delivery") 300.0 else 0.0
        val total = unitPrice * quantity + deliveryFee

        val order = Order(
            userId = userId,
            orderDate = System.currentTimeMillis(),
            status = "processing",
            fulfilment = fulfilment,
            deliveryAddressId = addressId,
            promoId = null,
            totalAmount = total,
            notes = null
        )
        val orderId = orderDao.insert(order)

        orderItemDao.insert(
            OrderItem(
                orderId = orderId,
                productId = productId,
                quantity = quantity,
                paperType = material,
                size = size,
                unitPrice = unitPrice,
                designPath = designPath,
                customText = customText
            )
        )

        notificationDao.insert(
            Notification(
                userId = userId,
                title = "Order confirmed",
                message = "Your order #$orderId has been received and is being processed.",
                type = "order",
                isRead = false,
                createdAt = System.currentTimeMillis()
            )
        )

        return orderId
    }

    suspend fun getOrdersByUser(userId: Long): List<Order> = orderDao.getByUser(userId)

    suspend fun getOrderById(orderId: Long): Order? = orderDao.getById(orderId)

    suspend fun getItemsForOrder(orderId: Long) = orderItemDao.getByOrder(orderId)

    suspend fun cancelOrder(orderId: Long) {
        val order = orderDao.getById(orderId) ?: return
        orderDao.update(order.copy(status = "cancelled"))
    }
}
