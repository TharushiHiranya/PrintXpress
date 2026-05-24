package com.hiranya.printxpress.data.repository

import com.hiranya.printxpress.data.dao.NotificationDao
import com.hiranya.printxpress.data.entity.Notification

class NotificationRepository(private val notificationDao: NotificationDao) {
    suspend fun getByUser(userId: Long): List<Notification> = notificationDao.getByUser(userId)

    suspend fun markRead(notificationId: Long) = notificationDao.markRead(notificationId)

    suspend fun markAllRead(userId: Long) = notificationDao.markAllRead(userId)
}
