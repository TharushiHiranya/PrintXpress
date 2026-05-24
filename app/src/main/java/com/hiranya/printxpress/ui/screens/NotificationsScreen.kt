package com.hiranya.printxpress.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LocalOffer
import androidx.compose.material.icons.rounded.LocalShipping
import androidx.compose.material.icons.rounded.Print
import androidx.compose.material.icons.rounded.Receipt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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

// Notification data model
private data class NotifItem(
    val unread: Boolean,
    val icon: ImageVector,
    val title: String,
    val body: String,
    val time: String
)

private val notifications = listOf(
    NotifItem(true, Icons.Rounded.Print, "Order #1042 is printing", "Your premium matt cards are on the press. Est. ready by 4 pm today.", "10 min ago"),
    NotifItem(true, Icons.Rounded.LocalOffer, "20% off business cards", "Use CARDS20 at checkout. Expires Sun 31 May.", "2 hours ago"),
    NotifItem(true, Icons.Rounded.LocalShipping, "Order #1031 is on the way", "Your driver is 8 minutes away.", "Yesterday"),
    NotifItem(false, Icons.Rounded.CheckCircle, "Order #1019 completed", "Thanks for ordering with PrintXpress. Rate your experience.", "Mon, 19 May"),
    NotifItem(false, Icons.Rounded.Receipt, "Receipt for order #1019", "Your invoice has been emailed to sarah@nilu.lk", "Mon, 19 May"),
    NotifItem(false, Icons.Rounded.Info, "New: T-shirt printing is here", "DTF and screen-printed tees, starting at LKR 1,650.", "Thu, 8 May")
)

private val filterOptions = listOf("All", "Orders", "Offers", "System")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    navController: NavController,
    viewModel: NotificationsViewModel = viewModel()
) {
    var selectedFilter by remember { mutableIntStateOf(0) }
    val notificationList = viewModel.notifications.value

    LaunchedEffect(Unit) { viewModel.load() }

    MainScaffold(navController) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Top bar with mark-all-read action
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Notifications", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
                TextButton(onClick = { viewModel.markAllRead() }) {
                    Text("Mark all read", style = MaterialTheme.typography.bodyMedium, color = Accent)
                }
            }

            // Filter chips row
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterOptions.size) { index ->
                    FilterChip(
                        selected = selectedFilter == index,
                        onClick = { selectedFilter = index },
                        label = { Text(filterOptions[index]) },
                        shape = RoundedCornerShape(50.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Accent,
                            selectedLabelColor = OnAccent,
                            containerColor = Background,
                            labelColor = TextPrimary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = selectedFilter == index,
                            selectedBorderColor = Accent,
                            borderColor = Divider
                        )
                    )
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

@Composable
private fun NotifRow(notif: NotifItem) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // Accent left stripe for unread notifications
        Box(
            modifier = Modifier
                .width(4.dp)
                .then(Modifier)
                .background(if (notif.unread) Accent else Background)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (notif.unread) AccentContainer else Background)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Icon in a bordered circle container
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Background, RoundedCornerShape(10.dp))
                    .border(1.dp, if (notif.unread) Accent else Divider, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(notif.icon, contentDescription = null, tint = Accent, modifier = Modifier.size(20.dp))
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        notif.title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = if (notif.unread) FontWeight.SemiBold else FontWeight.Normal
                        ),
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    if (notif.unread) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Accent, androidx.compose.foundation.shape.CircleShape)
                        )
                    }
                }
                Text(notif.body, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                Text(notif.time, style = MaterialTheme.typography.labelSmall, color = TextDisabled)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationsScreenPreview() {
    PrintXpressTheme {
        NotificationsScreen(navController = rememberNavController())
    }
}
