package com.hiranya.printxpress.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocalOffer
import androidx.compose.material.icons.rounded.Receipt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hiranya.printxpress.data.entity.Notification
import com.hiranya.printxpress.viewmodel.HomeViewModel
import com.hiranya.printxpress.viewmodel.NotificationsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.hiranya.printxpress.ui.components.MainScaffold
import com.hiranya.printxpress.ui.theme.Accent
import com.hiranya.printxpress.ui.theme.AccentContainer
import com.hiranya.printxpress.ui.theme.Background
import com.hiranya.printxpress.ui.theme.Divider
import com.hiranya.printxpress.ui.theme.OnAccent
import com.hiranya.printxpress.ui.theme.PrintXpressTheme
import com.hiranya.printxpress.ui.theme.TextDisabled
import com.hiranya.printxpress.ui.theme.TextPrimary
import com.hiranya.printxpress.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    navController: NavController,
    viewModel: NotificationsViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel()
) {
    val notificationList by viewModel.notifications
    val unreadCount by homeViewModel.unreadCount

    LaunchedEffect(Unit) { viewModel.load() }

    NotificationsContent(
        notificationList = notificationList,
        unreadCount = unreadCount,
        onMarkAllRead = { viewModel.markAllRead() },
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsContent(
    notificationList: List<Notification>,
    unreadCount: Int = 0,
    onMarkAllRead: () -> Unit,
    navController: NavController? = null
) {
    MainScaffold(navController ?: rememberNavController(), unreadCount = unreadCount) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Top bar with mark-all-read action
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Notifications", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
                TextButton(onClick = onMarkAllRead) {
                    Text("Mark all read", style = MaterialTheme.typography.bodyMedium, color = Accent)
                }
            }

            // Notification list from Room.
            LazyColumn {
                items(notificationList) { notif ->
                    val timeStr = SimpleDateFormat("dd MMM · HH:mm", Locale.getDefault()).format(Date(notif.createdAt))
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(if (!notif.isRead) AccentContainer else Background)
                                .then(
                                    if (!notif.isRead) Modifier
                                    else Modifier
                                )
                        ) {
                            // Accent left border for unread rows.
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .fillMaxWidth()
                                    .background(if (!notif.isRead) Accent else Background)
                            )
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                val icon = if (notif.type == "promo") Icons.Rounded.LocalOffer else Icons.Rounded.Receipt
                                Icon(icon, contentDescription = null, tint = Accent, modifier = Modifier.size(24.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        notif.title,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = TextPrimary,
                                        fontWeight = if (!notif.isRead) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                    Text(notif.message, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                                    Text(timeStr, style = MaterialTheme.typography.labelSmall, color = TextDisabled)
                                }
                            }
                        }
                        HorizontalDivider(color = Divider)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationsScreenPreview() {
    PrintXpressTheme {
        NotificationsContent(
            notificationList = listOf(
                Notification(1, 1, "20% Off", "Use code CARDS20", "promo", false, System.currentTimeMillis()),
                Notification(2, 1, "Order Shipped", "Your order is on the way", "order", true, System.currentTimeMillis() - 3600000)
            ),
            onMarkAllRead = {}
        )
    }
}
