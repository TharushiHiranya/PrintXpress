package com.hiranya.printxpress.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Article
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.Checkroom
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.LocalCafe
import androidx.compose.material.icons.rounded.Panorama
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hiranya.printxpress.data.entity.Product
import com.hiranya.printxpress.ui.Screen
import com.hiranya.printxpress.ui.components.MainScaffold
import com.hiranya.printxpress.ui.theme.Accent
import com.hiranya.printxpress.ui.theme.AccentContainer
import com.hiranya.printxpress.ui.theme.Background
import com.hiranya.printxpress.ui.theme.Divider
import com.hiranya.printxpress.ui.theme.OnAccent
import com.hiranya.printxpress.ui.theme.PrintXpressTheme
import com.hiranya.printxpress.ui.theme.StatusRed
import com.hiranya.printxpress.ui.theme.TextDisabled
import com.hiranya.printxpress.ui.theme.TextPrimary
import com.hiranya.printxpress.ui.theme.TextSecondary
import com.hiranya.printxpress.viewmodel.ProductViewModel

// Maps the category iconRef to an icon for the product placeholder image.
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    navController: NavController,
    categoryId: Long,
    initialSearchQuery: String? = null,
    viewModel: ProductViewModel = viewModel(),
    homeViewModel: com.hiranya.printxpress.viewmodel.HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val products by viewModel.products
    val searchQuery by viewModel.searchQuery
    val savedDesignIds by viewModel.savedDesignIds
    val unreadCount by homeViewModel.unreadCount

    // Load products for this category on first composition.
    LaunchedEffect(categoryId, initialSearchQuery) {
        viewModel.loadProductsByCategory(categoryId, initialSearchQuery)
    }

    val isTopLevel = categoryId == 0L
    
    if (isTopLevel) {
        MainScaffold(navController, unreadCount = unreadCount) { paddingValues ->
            ProductListContent(
                products = products,
                searchQuery = searchQuery,
                savedDesignIds = savedDesignIds,
                onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
                onBack = null,
                onProductClick = { navController.navigate(Screen.ProductDetail.withId(it)) },
                onSaveClick = { viewModel.toggleSaveProduct(it) },
                modifier = Modifier.padding(paddingValues)
            )
        }
    } else {
        ProductListContent(
            products = products,
            searchQuery = searchQuery,
            savedDesignIds = savedDesignIds,
            onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
            onBack = { navController.popBackStack() },
            onProductClick = { navController.navigate(Screen.ProductDetail.withId(it)) },
            onSaveClick = { viewModel.toggleSaveProduct(it) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListContent(
    products: List<Product>,
    searchQuery: String,
    savedDesignIds: Set<String>,
    onSearchQueryChanged: (String) -> Unit,
    onBack: (() -> Unit)?,
    onProductClick: (Long) -> Unit,
    onSaveClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val content: @Composable (PaddingValues) -> Unit = { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            if (onBack == null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("All products", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
                }
            } else {
                Spacer(Modifier.height(12.dp))
            }

            // Search field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(50.dp),
                placeholder = { Text("Search products...", color = TextDisabled) },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null, tint = TextSecondary) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Accent,
                    unfocusedBorderColor = Divider,
                    cursorColor = Accent,
                    focusedLabelColor = Accent
                )
            )

            // 2-column product grid from real Room data
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val rows = products.chunked(2)
                items(rows) { rowProducts ->
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        rowProducts.forEach { product ->
                            ProductCard(
                                product = product,
                                isSaved = savedDesignIds.contains(product.name),
                                modifier = Modifier.weight(1f),
                                onOrderClick = { onProductClick(product.productId) },
                                onSaveClick = { onSaveClick(product) }
                            )
                        }
                        if (rowProducts.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }

    if (onBack != null) {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    title = { Text("Products") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
                )
            }
        ) { innerPadding ->
            content(innerPadding)
        }
    } else {
        // Top-level, avoid nested Scaffold to prevent double top padding
        Box(modifier = modifier) {
            content(PaddingValues(0.dp))
        }
    }
}

@Composable
private fun ProductCard(
    product: Product,
    isSaved: Boolean,
    modifier: Modifier = Modifier,
    onOrderClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    OutlinedCard(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Divider),
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(containerColor = Background)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(AccentContainer, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(iconForRef(product.imageRef), contentDescription = null, tint = Accent, modifier = Modifier.size(44.dp))
                
                // Save button at top right
                IconButton(
                    onClick = onSaveClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(32.dp)
                        .background(Background.copy(alpha = 0.6f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        contentDescription = "Save",
                        tint = if (isSaved) StatusRed else Accent,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(product.name, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
            Text(product.description, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            Text("From LKR ${product.basePrice.toLong()}", style = MaterialTheme.typography.bodyMedium, color = Accent)
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = onOrderClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50.dp),
                border = BorderStroke(1.dp, Accent)
            ) {
                Text("Order", color = Accent)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductListScreenPreview() {
    PrintXpressTheme {
        ProductListContent(
            products = listOf(
                Product(1, 1, "Premium matt cards", "Desc", 650.0, "Soft Touch", "badge", "", ""),
                Product(2, 1, "Event poster", "Desc", 850.0, "Glossy", "image", "", "")
            ),
            searchQuery = "",
            savedDesignIds = emptySet(),
            onSearchQueryChanged = {},
            onBack = {},
            onProductClick = {},
            onSaveClick = {}
        )
    }
}
