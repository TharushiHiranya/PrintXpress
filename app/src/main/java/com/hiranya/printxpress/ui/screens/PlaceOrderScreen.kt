package com.hiranya.printxpress.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.LocalOffer
import androidx.compose.material.icons.rounded.LocalShipping
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Receipt
import androidx.compose.material.icons.rounded.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hiranya.printxpress.ui.Screen
import com.hiranya.printxpress.viewmodel.OrderViewModel
import com.hiranya.printxpress.ui.theme.Accent
import com.hiranya.printxpress.ui.theme.AccentContainer
import com.hiranya.printxpress.ui.theme.Background
import com.hiranya.printxpress.ui.theme.Divider
import com.hiranya.printxpress.ui.theme.OnAccent
import com.hiranya.printxpress.ui.theme.PrintXpressTheme
import com.hiranya.printxpress.ui.theme.StatusGreyBg
import com.hiranya.printxpress.ui.theme.TextPrimary
import com.hiranya.printxpress.ui.theme.TextSecondary
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceOrderScreen(
    navController: NavController,
    productId: Long,
    orderViewModel: OrderViewModel = viewModel()
) {
    var currentStep by remember { mutableIntStateOf(1) }
    var deliveryMethod by remember { mutableStateOf("delivery") }
    var selectedAddress by remember { mutableIntStateOf(0) }

    val isLoading = orderViewModel.isLoading.value

    val stepLabels = listOf("Summary", "Delivery", "Confirm")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Place order") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // Step indicator — always visible above the scrollable content
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                stepLabels.forEachIndexed { index, label ->
                    val stepNum = index + 1
                    val isDone = currentStep > stepNum
                    val isActive = currentStep == stepNum

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    when {
                                        isDone || isActive -> Accent
                                        else -> Background
                                    },
                                    CircleShape
                                )
                                .then(
                                    if (!isDone && !isActive) Modifier.then(
                                        Modifier.background(Background, CircleShape)
                                    ) else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isDone) {
                                Icon(Icons.Rounded.Check, contentDescription = null, tint = OnAccent, modifier = Modifier.size(18.dp))
                            } else {
                                Text(
                                    "$stepNum",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isActive) OnAccent else TextSecondary
                                )
                            }
                        }
                        Text(
                            label,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isDone || isActive) Accent else TextSecondary
                        )
                    }

                    // Connector line between steps (not after the last step)
                    if (index < stepLabels.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp, vertical = 18.dp),
                            color = if (currentStep > stepNum) Accent else Divider
                        )
                    }
                }
            }

            // Step content
            when (currentStep) {
                1 -> Step1Body(
                    onNext = { currentStep = 2 }
                )
                2 -> Step2Body(
                    deliveryMethod = deliveryMethod,
                    onDeliveryMethodChange = { deliveryMethod = it },
                    selectedAddress = selectedAddress,
                    onAddressSelect = { selectedAddress = it },
                    onBack = { currentStep = 1 },
                    onNext = { currentStep = 3 }
                )
                3 -> Step3Body(
                    deliveryMethod = deliveryMethod,
                    isLoading = isLoading,
                    onBack = { currentStep = 2 },
                    onPlace = {
                        orderViewModel.placeOrder(
                            productId = productId,
                            quantity = 1,
                            size = "A4",
                            material = "Matte",
                            designPath = null,
                            customText = null,
                            fulfilment = deliveryMethod,
                            addressId = null
                        ) { orderId ->
                            navController.navigate(Screen.OrderConfirmation.withId(orderId)) {
                                popUpTo(Screen.PlaceOrder.route) { inclusive = true }
                            }
                        }
                    }
                )
            }
        }
    }
}

// Step 1: Order summary
@Composable
private fun Step1Body(onNext: () -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Text("Items in your order", style = MaterialTheme.typography.titleMedium, color = TextPrimary) }

        // Product item row
        item {
            OutlinedCard(
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Divider),
                colors = CardDefaults.cardColors(containerColor = Background),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(AccentContainer, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.Badge, contentDescription = null, tint = Accent, modifier = Modifier.size(30.dp))
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Premium matt cards", style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
                            Text("LKR 2,400", style = MaterialTheme.typography.bodyLarge, color = Accent)
                        }
                        // Spec chips
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            items(listOf("Standard 90×55", "350gsm matt", "Both sides", "Qty 200")) { spec ->
                                Surface(shape = RoundedCornerShape(999.dp), color = StatusGreyBg) {
                                    Text(
                                        spec,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSecondary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        // File name indicator
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Rounded.Description, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
                            Text("nilu-cards-final.pdf", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }
                    }
                }
            }
        }

        // Promo code row
        item {
            OutlinedCard(
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Accent),
                colors = CardDefaults.cardColors(containerColor = AccentContainer),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(14.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.LocalOffer, contentDescription = null, tint = Accent, modifier = Modifier.size(20.dp))
                        Column {
                            Text("Have a promo code?", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                            Text("Apply at checkout for discounts", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }
                    }
                    Text("Apply", style = MaterialTheme.typography.bodyMedium, color = Accent)
                }
            }
        }

        // Order totals
        item {
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
                        Text("LKR 2,400", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Background)
                    )
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Order total", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                        Text("LKR 2,400", style = MaterialTheme.typography.titleLarge, color = Accent)
                    }
                }
            }
        }

        item { Spacer(Modifier.height(16.dp)) }

        item {
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Accent)
            ) {
                Text("Next", color = OnAccent)
            }
        }
    }
}

// Step 2: Delivery method and address
@Composable
private fun Step2Body(
    deliveryMethod: String,
    onDeliveryMethodChange: (String) -> Unit,
    selectedAddress: Int,
    onAddressSelect: (Int) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "How would you like to receive your order?",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
        }

        // Delivery vs pickup cards
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DeliveryTypeCard(
                    icon = { Icon(Icons.Rounded.LocalShipping, contentDescription = null, tint = Accent) },
                    title = "Delivery",
                    subtitle = "To your address",
                    price = "LKR 350",
                    selected = deliveryMethod == "delivery",
                    modifier = Modifier.weight(1f),
                    onClick = { onDeliveryMethodChange("delivery") }
                )
                DeliveryTypeCard(
                    icon = { Icon(Icons.Rounded.Store, contentDescription = null, tint = Accent) },
                    title = "Pickup",
                    subtitle = "At our Colombo store",
                    price = "Free",
                    selected = deliveryMethod == "pickup",
                    modifier = Modifier.weight(1f),
                    onClick = { onDeliveryMethodChange("pickup") }
                )
            }
        }

        // Address selection — only shown for delivery
        if (deliveryMethod == "delivery") {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Delivery address", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    Text("+ Add new", style = MaterialTheme.typography.bodyMedium, color = Accent)
                }
            }

            items(2) { index ->
                val isSelected = selectedAddress == index
                OutlinedCard(
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, if (isSelected) Accent else Divider),
                    colors = CardDefaults.cardColors(containerColor = if (isSelected) AccentContainer else Background),
                    elevation = CardDefaults.cardElevation(0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAddressSelect(index) }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Radio button indicator
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(if (isSelected) Accent else Background, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) Box(modifier = Modifier.size(8.dp).background(OnAccent, CircleShape))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(if (index == 0) "Home" else "Studio", style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
                            Text(
                                if (index == 0) "42/3 Galle Road, Bambalapitiya, Colombo 04" else "Nilu Atelier, 18 Horton Pl, Colombo 07",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                        Icon(Icons.Rounded.LocationOn, contentDescription = null, tint = Accent, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }

        item { Spacer(Modifier.height(16.dp)) }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Divider)
                ) {
                    Text("Back", color = TextSecondary)
                }
                Button(
                    onClick = onNext,
                    modifier = Modifier
                        .weight(2f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Accent)
                ) {
                    Text("Next", color = OnAccent)
                }
            }
        }
    }
}

@Composable
private fun DeliveryTypeCard(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    price: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedCard(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(if (selected) 2.dp else 1.dp, if (selected) Accent else Divider),
        colors = CardDefaults.cardColors(containerColor = if (selected) AccentContainer else Background),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Background, RoundedCornerShape(12.dp))
                        .then(Modifier.background(Background, RoundedCornerShape(12.dp))),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }
                Text(title, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
                Text(subtitle, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                Text(price, style = MaterialTheme.typography.labelMedium, color = Accent)
            }
            if (selected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(20.dp)
                        .background(Accent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.Check, contentDescription = null, tint = OnAccent, modifier = Modifier.size(12.dp))
                }
            }
        }
    }
}

// Step 3: Order review and confirmation
@Composable
private fun Step3Body(
    deliveryMethod: String,
    isLoading: Boolean = false,
    onBack: () -> Unit,
    onPlace: () -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Confirm your order", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                Text("Review the details before placing your order.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            }
        }

        // Summary details card
        item {
            OutlinedCard(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Divider),
                colors = CardDefaults.cardColors(containerColor = Background),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SummaryRow("Items", "1 product · 200 pieces")
                    HorizontalDivider(color = Divider)
                    SummaryRow("Method", if (deliveryMethod == "delivery") "Delivery" else "Store pickup")
                    SummaryRow("Address", if (deliveryMethod == "delivery") "42/3 Galle Road, Colombo 04" else "42 Stratford Ave, Colombo 06")
                    SummaryRow("Payment", "Cash on delivery")
                }
            }
        }

        // Totals in accent container
        item {
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
                        Text("LKR 2,400", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Delivery", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                        Text(if (deliveryMethod == "delivery") "LKR 350" else "Free", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                    }
                    HorizontalDivider(color = Divider)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                        Text(if (deliveryMethod == "delivery") "LKR 2,750" else "LKR 2,400", style = MaterialTheme.typography.titleLarge, color = Accent)
                    }
                }
            }
        }

        // Confirmation checkbox
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .background(Accent, RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.Check, contentDescription = null, tint = OnAccent, modifier = Modifier.size(14.dp))
                }
                Text(
                    "I confirm my files are print-ready and accept PrintXpress's return policy.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }

        item { Spacer(Modifier.height(16.dp)) }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Divider)
                ) {
                    Text("Back", color = TextSecondary)
                }
                Button(
                    onClick = onPlace,
                    modifier = Modifier
                        .weight(2f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Accent),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = OnAccent, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Text("Place order", color = OnAccent)
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        Text(value, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceOrderScreenPreview() {
    PrintXpressTheme {
        PlaceOrderScreen(navController = rememberNavController(), productId = 1L)
    }
}
