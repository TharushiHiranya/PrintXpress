package com.hiranya.printxpress.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Receipt
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hiranya.printxpress.data.entity.Order
import com.hiranya.printxpress.ui.Screen
import com.hiranya.printxpress.ui.components.MainScaffold
import com.hiranya.printxpress.ui.theme.Accent
import com.hiranya.printxpress.ui.theme.AccentContainer
import com.hiranya.printxpress.ui.theme.Background
import com.hiranya.printxpress.ui.theme.Divider
import com.hiranya.printxpress.ui.theme.PrintXpressTheme
import com.hiranya.printxpress.ui.theme.StatusAmber
import com.hiranya.printxpress.ui.theme.StatusAmberBg
import com.hiranya.printxpress.ui.theme.StatusBlue
import com.hiranya.printxpress.ui.theme.StatusBlueBg
import com.hiranya.printxpress.ui.theme.StatusGreen
import com.hiranya.printxpress.ui.theme.StatusGreenBg
import com.hiranya.printxpress.ui.theme.StatusGreyBg
import com.hiranya.printxpress.ui.theme.StatusRed
import com.hiranya.printxpress.ui.theme.StatusRedBg
import com.hiranya.printxpress.ui.theme.TextPrimary
import com.hiranya.printxpress.ui.theme.TextSecondary
import com.hiranya.printxpress.viewmodel.OrderViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val activeStatuses = setOf("processing", "printing", "ready for pickup", "out for delivery")
private val tabLabels = listOf("All", "Active", "Completed", "Cancelled")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(navController: NavController, viewModel: OrderViewModel = viewModel()) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val allOrders = viewModel.orders.value

    LaunchedEffect(Unit) { viewModel.loadOrders() }

    val visibleOrders = when (selectedTab) {
        1 -> allOrders.filter { it.status in activeStatuses }
        2 -> allOrders.filter { it.status == "completed" }
        3 -> allOrders.filter { it.status == "cancelled" }
        else -> allOrders
    }

    MainScaffold(navController) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("My orders", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(AccentContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.Search, contentDescription = "Search", tint = Accent)
                }
            }

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Background,
                contentColor = Accent
            ) {
                tabLabels.forEachIndexed { index, label ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                label,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (selectedTab == index) Accent else TextSecondary
                            )
                        }
                    )
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (visibleOrders.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 64.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(Icons.Rounded.Receipt, contentDescription = null, tint = AccentContainer, modifier = Modifier.size(80.dp))
                            Text("No orders here", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                            if (selectedTab == 0) {
                                Spacer(Modifier.height(4.dp))
                                Button(
                                    onClick = { navController.navigate(Screen.ProductList.withId(1L)) },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Accent)
                                ) {
                                    Text("Browse products")
                                }
                            }
                        }
                    }
                } else {
                    items(visibleOrders) { order ->
                        OrderCard(order = order, navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderCard(order: Order, navController: NavController) {
    val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(order.orderDate))
    OutlinedCard(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Divider),
        colors = CardDefaults.cardColors(containerColor = Background),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(AccentContainer, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.Receipt, contentDescription = null, tint = Accent, modifier = Modifier.size(18.dp))
                    }
                    Column {
                        Text("Order #${order.orderId}", style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
                        Text(dateStr, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    }
                }
                StatusBadge(order.status)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Divider)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Total", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    Text("LKR ${order.totalAmount.toLong()}", style = MaterialTheme.typography.titleMedium, color = Accent)
                }
                IconButton(onClick = { navController.navigate(Screen.OrderDetail.withId(order.orderId)) }) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("View details", style = MaterialTheme.typography.bodyMedium, color = Accent)
                        Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = Accent, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

// Status badge — reusable across screens.
@Composable
fun StatusBadge(status: String) {
    val (bg, fg) = when (status.lowercase()) {
        "processing" -> AccentContainer to Accent
        "printing" -> StatusAmberBg to StatusAmber
        "ready for pickup" -> StatusGreenBg to StatusGreen
        "out for delivery" -> StatusBlueBg to StatusBlue
        "completed" -> StatusGreyBg to TextSecondary
        else -> StatusRedBg to StatusRed
    }
    Surface(shape = RoundedCornerShape(50.dp), color = bg) {
        Text(
            status.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelSmall,
            color = fg,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OrdersScreenPreview() {
    PrintXpressTheme {
        OrdersScreen(navController = rememberNavController())
    }
}
