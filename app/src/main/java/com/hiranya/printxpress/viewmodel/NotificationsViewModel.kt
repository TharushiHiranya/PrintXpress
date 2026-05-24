package com.hiranya.printxpress.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hiranya.printxpress.data.PrintXpressDatabase
import com.hiranya.printxpress.data.entity.Notification
import com.hiranya.printxpress.data.repository.NotificationRepository
import com.hiranya.printxpress.data.util.SessionManager
import kotlinx.coroutines.launch

class NotificationsViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: NotificationRepository

    private val _notifications = mutableStateOf<List<Notification>>(emptyList())
    val notifications: State<List<Notification>> = _notifications

    init {
        val db = PrintXpressDatabase.getDatabase(application)
        repo = NotificationRepository(db.notificationDao())
    }

    fun load() {
        val userId = SessionManager.getUserId() ?: return
        viewModelScope.launch {
            _notifications.value = repo.getByUser(userId)
        }
    }

    fun markRead(notificationId: Long) {
        viewModelScope.launch {
            repo.markRead(notificationId)
            load()
        }
    }

    fun markAllRead() {
        val userId = SessionManager.getUserId() ?: return
        viewModelScope.launch {
            repo.markAllRead(userId)
            load()
        }
    }
}
