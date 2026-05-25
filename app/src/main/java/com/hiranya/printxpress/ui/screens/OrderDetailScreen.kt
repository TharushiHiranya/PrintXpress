package com.hiranya.printxpress.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Article
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Checkroom
import androidx.compose.material.icons.rounded.HelpOutline
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.LocalCafe
import androidx.compose.material.icons.rounded.LocalShipping
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Panorama
import androidx.compose.material.icons.rounded.Print
import androidx.compose.material.icons.rounded.Receipt
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Store
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import com.hiranya.printxpress.data.entity.Address
import com.hiranya.printxpress.data.entity.Order
import com.hiranya.printxpress.data.entity.OrderItem
import com.hiranya.printxpress.ui.Screen
import com.hiranya.printxpress.viewmodel.OrderViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.hiranya.printxpress.ui.theme.Accent
import com.hiranya.printxpress.ui.theme.AccentContainer
import com.hiranya.printxpress.ui.theme.Background
import com.hiranya.printxpress.ui.theme.Divider
import com.hiranya.printxpress.ui.theme.OnAccent
import com.hiranya.printxpress.ui.theme.PrintXpressTheme
import com.hiranya.printxpress.ui.theme.StatusGreyBg
import com.hiranya.printxpress.ui.theme.StatusRed
import com.hiranya.printxpress.ui.theme.TextDisabled
import com.hiranya.printxpress.ui.theme.TextPrimary
import com.hiranya.printxpress.ui.theme.TextSecondary

// Maps the iconRef string stored in OrderItem to a Compose icon.
private fun iconForRef(ref: String): ImageVector = when (ref) {
    "badge" -> Icons.Rounded.Badge
    "image" -> Icons.Rounded.Image
    "panorama" -> Icons.Rounded.Panorama
    "article" -> Icons.Rounded.Article
    "stars", "star" -> Icons.Rounded.Star
    "local_cafe" -> Icons.Rounded.LocalCafe
    "checkroom" -> Icons.Rounded.Checkroom
    else -> Icons.Rounded.Badge
}

// Status stages for the order progress stepper
private data class Stage(val label: String, val icon: ImageVector, val state: String)

private val orderStages = listOf(
    Stage("Processing", Icons.Rounded.Receipt, "done"),
    Stage("Printing", Icons.Rounded.Print, "active"),
    Stage("Ready", Icons.Rounded.CheckCircle, "future"),
    Stage("Delivering", Icons.Rounded.LocalShipping, "future"),
    Stage("Completed", Icons.Rounded.CheckCircle, "future")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    navController: NavController,
    orderId: Long,
    viewModel: OrderViewModel = viewModel()
) {
    val order by viewModel.selectedOrder
    val items by viewModel.orderItems
    val address by viewModel.orderAddress

    LaunchedEffect(orderId) { viewModel.loadOrder(orderId) }

    OrderDetailContent(
        orderId = orderId,
        order = order,
        items = items,
        address = address,
        onBack = { navController.popBackStack() },
        onCancelOrder = {
            viewModel.cancelOrder(orderId)
            navController.popBackStack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailContent(
    orderId: Long,
    order: Order?,
    items: List<OrderItem>,
    address: Address?,
    onBack: () -> Unit,
    onCancelOrder: () -> Unit
) {
    val status = order?.status ?: "processing"
    val currentStageIndex = when (status.lowercase()) {
        "processing" -> 0
        "printing" -> 1
        "ready for pickup" -> 2
        "out for delivery" -> 3
        "completed" -> 4
        else -> 0
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order #$orderId") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Current status hero card
            item {
                val dateStr = order?.let { SimpleDateFormat("dd MMM yyyy · HH:mm", Locale.getDefault()).format(Date(it.orderDate)) } ?: ""
                OutlinedCard(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AccentContainer),
                    elevation = CardDefaults.cardElevation(0.dp),
                    border = BorderStroke(0.dp, AccentContainer)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                "STATUS",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                            Text(status.replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.titleLarge, color = Accent)
                            Text(dateStr, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Accent, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Rounded.Receipt, contentDescription = null, tint = OnAccent, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }

            // Status progress stepper
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    orderStages.forEachIndexed { index, stage ->
                        val isDone = index < currentStageIndex
                        val isActive = index == currentStageIndex

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        if (isDone || isActive) Accent else Background,
                                        CircleShape
                                    )
                                    .then(
                                        if (!isDone && !isActive)
                                            Modifier.then(Modifier) // border via outline below
                                        else Modifier
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = stage.icon,
                                    contentDescription = null,
                                    tint = if (isDone || isActive) OnAccent else TextDisabled,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                stage.label,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isDone || isActive) Accent else TextDisabled
                            )
                        }

                        // Connector line between stages
                        if (index < orderStages.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier
                                    .weight(0.5f)
                                    .padding(bottom = 18.dp),
                                color = if (index < 1) Accent else Divider
                            )
                        }
                    }
                }
            }

            // Items in this order
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Items in this order", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    if (items.isEmpty()) {
                        Text("No items loaded yet.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    }
                    items.forEach { item ->
                        OutlinedCard(
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, Divider),
                            colors = CardDefaults.cardColors(containerColor = Background),
                            elevation = CardDefaults.cardElevation(0.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .background(AccentContainer, RoundedCornerShape(12.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(iconForRef(item.productImageRef), contentDescription = null, tint = Accent, modifier = Modifier.size(28.dp))
                                    }
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(item.productName, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
                                            Text("LKR ${(item.unitPrice * item.quantity).toLong()}", style = MaterialTheme.typography.bodyLarge, color = Accent)
                                        }
                                        Text("Qty ${item.quantity} · ${item.size} · ${item.paperType}", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                        if (!item.customText.isNullOrBlank()) {
                                            Text("Custom text: ${item.customText}", style = MaterialTheme.typography.labelSmall, color = Accent)
                                        }
                                    }
                            }
                        }
                    }
                }
            }

            // Delivery details
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        if (order?.fulfilment == "delivery") "Delivery details" else "Pickup details",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    OutlinedCard(
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, Divider),
                        colors = CardDefaults.cardColors(containerColor = Background),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(AccentContainer, RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    if (order?.fulfilment == "delivery") Icons.Rounded.LocationOn else Icons.Rounded.Store,
                                    contentDescription = null,
                                    tint = Accent
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                if (order?.fulfilment == "delivery") {
                                    Text(address?.label ?: "Address", style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
                                    Text(
                                        address?.let { "${it.line1}, ${it.city}" } ?: "Loading address...",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextSecondary
                                    )
                                } else {
                                    Text("Store Pickup", style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
                                    Text("42 Stratford Ave, Colombo 06", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                                }
                            }
                        }
                    }
                }
            }

            // Order totals
            item {
                val total = order?.totalAmount ?: 0.0
                val deliveryFee = if (order?.fulfilment == "delivery") 300.0 else 0.0
                val subtotal = total - deliveryFee
                OutlinedCard(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = AccentContainer),
                    elevation = CardDefaults.cardElevation(0.dp),
                    border = BorderStroke(0.dp, AccentContainer)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Subtotal", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                            Text("LKR ${subtotal.toLong()}", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Delivery", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                            Text(if (deliveryFee > 0) "LKR ${deliveryFee.toLong()}" else "Free", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = Divider)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                            Text("LKR ${total.toLong()}", style = MaterialTheme.typography.titleLarge, color = Accent)
                        }
                    }
                }
            }

            // Cancel action — only shown when the order can still be modified.
            if (status.lowercase() in setOf("processing")) {
                item {
                    OutlinedButton(
                        onClick = onCancelOrder,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, StatusRed)
                    ) {
                        Text("Cancel order", color = StatusRed)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OrderDetailScreenPreview() {
    PrintXpressTheme {
        OrderDetailContent(
            orderId = 1042L,
            order = Order(1042L, 1, System.currentTimeMillis(), "printing", "delivery", null, null, 2750.0, null),
            items = listOf(
                OrderItem(1, 1042L, 1, "Premium Business Cards", "badge", 2, "Matte", "A4", 1200.0, null, "John Doe")
            ),
            address = Address(1, 1, "Home", "42/3 Galle Road", "Colombo 04", "00400"),
            onBack = {},
            onCancelOrder = {}
        )
    }
}
