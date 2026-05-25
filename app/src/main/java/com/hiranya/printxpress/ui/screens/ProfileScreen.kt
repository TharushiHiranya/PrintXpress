package com.hiranya.printxpress.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.FolderOpen
import androidx.compose.material.icons.rounded.Help
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import com.hiranya.printxpress.data.entity.User
import com.hiranya.printxpress.viewmodel.HomeViewModel
import com.hiranya.printxpress.viewmodel.ProfileViewModel
import com.hiranya.printxpress.ui.Screen
import com.hiranya.printxpress.ui.components.MainScaffold
import com.hiranya.printxpress.ui.theme.Accent
import com.hiranya.printxpress.ui.theme.AccentContainer
import com.hiranya.printxpress.ui.theme.Background
import com.hiranya.printxpress.ui.theme.Divider
import com.hiranya.printxpress.ui.theme.OnAccent
import com.hiranya.printxpress.ui.theme.PrintXpressTheme
import com.hiranya.printxpress.ui.theme.StatusRed
import com.hiranya.printxpress.ui.theme.StatusRedBg
import com.hiranya.printxpress.ui.theme.TextDisabled
import com.hiranya.printxpress.ui.theme.TextPrimary
import com.hiranya.printxpress.ui.theme.TextSecondary

// Menu item data for the navigation list
private data class MenuItem(val icon: ImageVector, val label: String, val route: String?)

private val menuItems = listOf(
    MenuItem(Icons.Rounded.LocationOn, "Delivery addresses", Screen.Addresses.route),
    MenuItem(Icons.Rounded.FolderOpen, "Saved designs", Screen.SavedDesigns.route),
    MenuItem(Icons.Rounded.MenuBook, "Print guidelines", Screen.PrintGuidelines.route),
    MenuItem(Icons.Rounded.Help, "FAQs and support", Screen.Faq.route),
    MenuItem(Icons.Rounded.Info, "About PrintXpress", Screen.About.route)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel()
) {
    val user by viewModel.user
    val orderCount by viewModel.orderCount
    val lifetimeSpend by viewModel.lifetimeSpend
    val tier by viewModel.tier
    val unreadCount by homeViewModel.unreadCount

    LaunchedEffect(Unit) { viewModel.load() }

    ProfileContent(
        user = user,
        orderCount = orderCount,
        lifetimeSpend = lifetimeSpend,
        tier = tier,
        unreadCount = unreadCount,
        onEditProfile = { navController.navigate(Screen.EditProfile.route) },
        onMenuItemClick = { route -> route?.let { navController.navigate(it) } },
        onLogout = {
            viewModel.logout()
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        },
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    user: User?,
    orderCount: Int = 0,
    lifetimeSpend: Double = 0.0,
    tier: String = "Bronze",
    unreadCount: Int = 0,
    onEditProfile: () -> Unit,
    onMenuItemClick: (String?) -> Unit,
    onLogout: () -> Unit,
    navController: NavController? = null
) {
    MainScaffold(navController ?: rememberNavController(), unreadCount = unreadCount) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Top bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Profile", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(AccentContainer, CircleShape)
                            .clickable { onEditProfile() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.Settings, contentDescription = "Settings", tint = Accent, modifier = Modifier.size(22.dp))
                    }
                }
            }

            // Avatar + name + email section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.size(84.dp)) {
                        // Avatar circle with initial
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(AccentContainer, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(user?.fullName?.first()?.uppercase() ?: "?", style = MaterialTheme.typography.titleLarge, color = Accent)
                        }
                    }
                    Spacer(Modifier.height(14.dp))
                    Text(user?.fullName ?: "...", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
                    Text(user?.email ?: user?.phone ?: "", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                }
            }

            // Stats card (orders, lifetime spend, tier)
            item {
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AccentContainer),
                    elevation = CardDefaults.cardElevation(0.dp),
                    border = BorderStroke(0.dp, AccentContainer)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(orderCount.toString(), style = MaterialTheme.typography.titleLarge, color = Accent)
                            Text("Orders", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }
                        Box(modifier = Modifier.width(1.dp).height(40.dp).background(Background))
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            val spendText = if (lifetimeSpend >= 1000) {
                                "LKR ${(lifetimeSpend / 1000).toInt()}k"
                            } else {
                                "LKR ${lifetimeSpend.toInt()}"
                            }
                            Text(spendText, style = MaterialTheme.typography.titleLarge, color = Accent)
                            Text("Lifetime", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }
                        Box(modifier = Modifier.width(1.dp).height(40.dp).background(Background))
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(tier, style = MaterialTheme.typography.titleLarge, color = Accent)
                            Text("Tier", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }
                    }
                }
            }

            // Menu list
            item {
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Divider),
                    colors = CardDefaults.cardColors(containerColor = Background),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column {
                        menuItems.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onMenuItemClick(item.route) }
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                horizontalArrangement = Arrangement.spacedBy(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(AccentContainer, RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(item.icon, contentDescription = null, tint = Accent, modifier = Modifier.size(20.dp))
                                }
                                Text(item.label, style = MaterialTheme.typography.bodyLarge, color = TextPrimary, modifier = Modifier.weight(1f))
                                Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = TextSecondary)
                            }
                            if (index < menuItems.size - 1) {
                                HorizontalDivider(color = Divider)
                            }
                        }
                    }
                }
            }

            // Log out row
            item {
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Divider),
                    colors = CardDefaults.cardColors(containerColor = Background),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLogout() }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(StatusRedBg, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Rounded.Logout, contentDescription = null, tint = StatusRed)
                        }
                        Text("Log out", style = MaterialTheme.typography.bodyLarge, color = StatusRed, modifier = Modifier.weight(1f))
                    }
                }
            }

            // App version note
            item {
                Text(
                    "PrintXpress v1.0.0 · Colombo, Sri Lanka",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextDisabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    PrintXpressTheme {
        ProfileContent(
            user = User(1, "Sarah Nilu", "sarah@nilu.lk", "0771234567", "", 0L),
            orderCount = 24,
            lifetimeSpend = 142000.0,
            tier = "Gold",
            onEditProfile = {},
            onMenuItemClick = {},
            onLogout = {}
        )
    }
}
