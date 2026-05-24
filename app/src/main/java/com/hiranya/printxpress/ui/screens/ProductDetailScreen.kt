package com.hiranya.printxpress.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hiranya.printxpress.ui.Screen
import com.hiranya.printxpress.viewmodel.ProductViewModel
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
fun ProductDetailScreen(
    navController: NavController,
    productId: Long,
    viewModel: ProductViewModel = viewModel()
) {
    var selectedSize by remember { mutableIntStateOf(0) }
    var selectedPaper by remember { mutableIntStateOf(0) }
    var qty by remember { mutableIntStateOf(1) }
    var designTab by remember { mutableIntStateOf(0) }
    var textContent by remember { mutableStateOf("") }

    val product = viewModel.selectedProduct.value

    LaunchedEffect(productId) { viewModel.loadProduct(productId) }

    val sizeOptions = product?.sizeOptions?.split(",")?.map { it.trim() }
        ?: listOf("A4", "A5", "A6")
    val paperOptions = product?.materialOptions?.split(",")?.map { it.trim() }
        ?: listOf("Matte", "Glossy")

    val productName = product?.name ?: "Loading..."
    val productPrice = product?.basePrice ?: 0.0
    val productDescription = product?.description ?: ""

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(productName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Favourite button in a circle container
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(40.dp)
                            .background(AccentContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.FavoriteBorder, contentDescription = "Save", tint = Accent)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        bottomBar = {
            // Sticky price and order button
            Surface(shadowElevation = 4.dp, modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Total", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        Text("LKR ${(productPrice * qty).toLong()}", style = MaterialTheme.typography.titleLarge, color = Accent)
                    }
                    Button(
                        onClick = { navController.navigate(Screen.PlaceOrder.withId(productId)) },
                        modifier = Modifier.height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Accent)
                    ) {
                        Text("Add to order", style = MaterialTheme.typography.labelMedium, color = OnAccent)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp)
        ) {
            // Hero image area with dot indicator
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(AccentContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Badge, contentDescription = null, tint = Accent, modifier = Modifier.size(92.dp))
                // Page indicator dots
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(18.dp)
                            .height(6.dp)
                            .background(Accent, RoundedCornerShape(3.dp))
                    )
                    repeat(3) {
                        Box(modifier = Modifier.size(6.dp).background(Divider, CircleShape))
                    }
                }
            }

            // Product name, price, and rating
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(productName, style = MaterialTheme.typography.titleLarge, color = TextPrimary)
                    Text("LKR ${productPrice.toLong()}", style = MaterialTheme.typography.titleLarge, color = Accent)
                }
                if (productDescription.isNotBlank()) {
                    Text(
                        productDescription,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            HorizontalDivider(color = Divider)

            Text(
                "Choose your options",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            // Size chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sizeOptions.size) { i ->
                    FilterChip(
                        selected = selectedSize == i,
                        onClick = { selectedSize = i },
                        label = { Text(sizeOptions[i]) },
                        shape = CircleShape,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Accent,
                            selectedLabelColor = OnAccent,
                            containerColor = Background,
                            labelColor = TextPrimary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = selectedSize == i,
                            selectedBorderColor = Accent,
                            borderColor = Divider
                        )
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Paper weight chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(paperOptions.size) { i ->
                    FilterChip(
                        selected = selectedPaper == i,
                        onClick = { selectedPaper = i },
                        label = { Text(paperOptions[i]) },
                        shape = CircleShape,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Accent,
                            selectedLabelColor = OnAccent,
                            containerColor = Background,
                            labelColor = TextPrimary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = selectedPaper == i,
                            selectedBorderColor = Accent,
                            borderColor = Divider
                        )
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Quantity selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Quantity", style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { if (qty > 1) qty-- },
                        modifier = Modifier.size(40.dp),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Divider)
                    ) {
                        Text("-", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    }
                    Text("$qty", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    OutlinedButton(
                        onClick = { qty++ },
                        modifier = Modifier.size(40.dp),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Divider)
                    ) {
                        Text("+", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    }
                }
            }

            // Design upload / text entry section
            Text(
                "Your design",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Tab strip to toggle between upload and text entry
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(1.dp, Divider, RoundedCornerShape(8.dp))
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(if (designTab == 0) AccentContainer else Background)
                        .clickable { designTab = 0 }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Upload file",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (designTab == 0) Accent else TextSecondary
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(if (designTab == 1) AccentContainer else Background)
                        .clickable { designTab = 1 }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Enter text",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (designTab == 1) Accent else TextSecondary
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Upload tab content
            if (designTab == 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .border(1.5.dp, Accent, RoundedCornerShape(14.dp))
                        .background(AccentContainer, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Rounded.Upload, contentDescription = null, tint = Accent, modifier = Modifier.size(32.dp))
                        Text("Tap to upload your design", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                        Text("PDF, PNG, JPG · up to 25 MB", style = MaterialTheme.typography.labelSmall, color = TextDisabled)
                    }
                }
            } else {
                // Text input tab
                OutlinedTextField(
                    value = textContent,
                    onValueChange = { textContent = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(120.dp),
                    placeholder = { Text("Type your custom text here...") },
                    maxLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Accent,
                        unfocusedBorderColor = Divider,
                        cursorColor = Accent,
                        focusedLabelColor = Accent
                    )
                )
            }

            // Turnaround and delivery info
            OutlinedCard(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = AccentContainer),
                elevation = CardDefaults.cardElevation(0.dp),
                border = androidx.compose.foundation.BorderStroke(0.dp, AccentContainer)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.Schedule, contentDescription = null, tint = Accent)
                    Column {
                        Text("Ready in 2–3 business days", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                        Text("Free pickup at store · Delivery from LKR 350", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductDetailScreenPreview() {
    PrintXpressTheme {
        ProductDetailScreen(navController = rememberNavController(), productId = 1L)
    }
}
