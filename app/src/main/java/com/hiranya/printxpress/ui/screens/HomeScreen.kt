package com.hiranya.printxpress.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.rounded.Article
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.Checkroom
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.LocalCafe
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Panorama
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hiranya.printxpress.R
import com.hiranya.printxpress.data.entity.Category
import com.hiranya.printxpress.data.entity.Promotion
import com.hiranya.printxpress.ui.Screen
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
import com.hiranya.printxpress.viewmodel.HomeViewModel

// Maps the iconRef string stored in Room to a Compose icon.
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

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = viewModel()) {
    val categories by viewModel.categories
    val activePromotions by viewModel.activePromotions
    val unreadCount by viewModel.unreadCount

    // Reload data every time this screen becomes visible.
    LaunchedEffect(Unit) { viewModel.load() }

    HomeContent(
        categories = categories,
        activePromotions = activePromotions,
        unreadCount = unreadCount,
        navController = navController,
        onCategoryClick = { catId -> navController.navigate(Screen.ProductList.withId(catId)) }
    )
}

@Composable
fun HomeContent(
    categories: List<Category>,
    activePromotions: List<Promotion>,
    unreadCount: Int,
    navController: NavController? = null,
    onCategoryClick: (Long) -> Unit = {}
) {
    MainScaffold(navController ?: rememberNavController()) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Logo + notification bell row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "PrintXpress",
                        modifier = Modifier.height(36.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(Modifier.weight(1f))
                    Box(contentAlignment = Alignment.TopEnd) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(AccentContainer, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Rounded.Notifications, contentDescription = "Notifications", tint = Accent)
                        }
                        // Show dot only when there are unread notifications.
                        if (unreadCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(Accent, CircleShape)
                                    .border(1.5.dp, Background, CircleShape)
                                    .align(Alignment.TopEnd)
                            )
                        }
                    }
                }
            }

            // Greeting text
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Good morning, Sarah", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
                    Text("What would you like to print today?", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                }
            }

            // Read-only search field
            item {
                val searchValue by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = searchValue,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(50.dp),
                    placeholder = { Text("Search products...", color = TextDisabled) },
                    leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null, tint = TextSecondary) },
                    singleLine = true,
                    readOnly = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Accent,
                        unfocusedBorderColor = Divider,
                        cursorColor = Accent,
                        focusedLabelColor = Accent
                    )
                )
            }

            // Offers section — only shown when there are active promotions.
            if (activePromotions.isNotEmpty()) {
                item {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Current offers", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                            Text("See all", style = MaterialTheme.typography.labelMedium, color = Accent)
                        }
                        Spacer(Modifier.height(8.dp))
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(activePromotions) { promo ->
                                OfferCard(promo)
                            }
                        }
                        Spacer(Modifier.height(24.dp))
                    }
                }
            }

            // Category grid built from real Room data.
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Browse by category", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                        Text("${categories.size} types", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                    }
                    Spacer(Modifier.height(8.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        categories.chunked(2).forEach { rowItems ->
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                rowItems.forEach { cat ->
                                    CategoryCard(
                                        category = cat,
                                        modifier = Modifier.weight(1f),
                                        onClick = { onCategoryClick(cat.categoryId) }
                                    )
                                }
                                if (rowItems.size == 1) Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun OfferCard(promo: Promotion) {
    OutlinedCard(
        modifier = Modifier.width(280.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AccentContainer),
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(0.dp, AccentContainer)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(Accent)
            )
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(promo.title, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
                Text(promo.description, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(shape = RoundedCornerShape(8.dp), color = Accent) {
                        Text(
                            promo.code,
                            style = MaterialTheme.typography.labelSmall,
                            color = OnAccent,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryCard(category: Category, modifier: Modifier = Modifier, onClick: () -> Unit) {
    OutlinedCard(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Divider),
        colors = CardDefaults.cardColors(containerColor = Background),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(AccentContainer, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(iconForRef(category.iconRef), contentDescription = null, tint = Accent, modifier = Modifier.size(28.dp))
            }
            Text(category.name, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    PrintXpressTheme {
        HomeContent(
            categories = listOf(
                Category(1, "Business cards", "Desc", "badge"),
                Category(2, "Posters", "Desc", "image")
            ),
            activePromotions = listOf(
                Promotion(1, "20% Off", "Description", 20, "CARDS20", 0, 0)
            ),
            unreadCount = 2
        )
    }
}
