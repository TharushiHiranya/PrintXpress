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
    MenuItem(Icons.Rounded.FolderOpen, "Saved designs", null),
    MenuItem(Icons.Rounded.MenuBook, "Print guidelines", Screen.PrintGuidelines.route),
    MenuItem(Icons.Rounded.Help, "FAQs and support", Screen.Faq.route),
    MenuItem(Icons.Rounded.Info, "About PrintXpress", null)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {
    val user = viewModel.user.value
    LaunchedEffect(Unit) { viewModel.load() }

    MainScaffold(navController) { paddingValues ->
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
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Profile", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(AccentContainer, CircleShape),
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
                        // Edit button overlaid at bottom-end
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(Accent, CircleShape)
                                .then(Modifier.align(Alignment.BottomEnd)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Rounded.Edit, contentDescription = null, tint = OnAccent, modifier = Modifier.size(14.dp))
                        }
                    }
                    Spacer(Modifier.height(14.dp))
                    Text(user?.fullName ?: "...", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
                    Text(user?.email ?: user?.phone ?: "", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    TextButton(onClick = { navController.navigate(Screen.EditProfile.route) }) {
                        Text("Edit profile", style = MaterialTheme.typography.bodyMedium, color = Accent)
                    }
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
                            Text("24", style = MaterialTheme.typography.titleLarge, color = Accent)
                            Text("Orders", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }
                        Box(modifier = Modifier.width(1.dp).height(40.dp).background(Background))
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("LKR 142k", style = MaterialTheme.typography.titleLarge, color = Accent)
                            Text("Lifetime", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }
                        Box(modifier = Modifier.width(1.dp).height(40.dp).background(Background))
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Gold", style = MaterialTheme.typography.titleLarge, color = Accent)
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
                                    .clickable { item.route?.let { navController.navigate(it) } }
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
                            .clickable {
                                viewModel.logout()
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
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
        ProfileScreen(navController = rememberNavController())
    }
}
