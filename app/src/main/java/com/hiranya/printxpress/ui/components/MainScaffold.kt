package com.hiranya.printxpress.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Receipt
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hiranya.printxpress.ui.Screen
import com.hiranya.printxpress.ui.theme.Accent
import com.hiranya.printxpress.ui.theme.Background
import com.hiranya.printxpress.ui.theme.Divider
import com.hiranya.printxpress.ui.theme.OnAccent
import com.hiranya.printxpress.ui.theme.StatusRed
import com.hiranya.printxpress.ui.theme.TextSecondary
import com.hiranya.printxpress.viewmodel.HomeViewModel

// Tab definition for the bottom navigation bar
private data class NavTab(
    val label: String,
    val icon: ImageVector,
    val route: String
)

private val navTabs = listOf(
    NavTab("Home", Icons.Rounded.Home, Screen.Home.route),
    NavTab("Products", Icons.Rounded.GridView, "product_list/0"),
    NavTab("Orders", Icons.Rounded.Receipt, Screen.Orders.route),
    NavTab("Alerts", Icons.Rounded.Notifications, Screen.Notifications.route),
    NavTab("Profile", Icons.Rounded.Person, Screen.Profile.route)
)

// Wraps any bottom-nav screen with the shared navigation bar and scaffold
@Composable
fun MainScaffold(
    navController: NavController,
    unreadCount: Int = 0,
    content: @Composable (PaddingValues) -> Unit
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Determine which tab is active by matching the current route or its prefix
    val selectedIndex = navTabs.indexOfFirst { tab ->
        currentRoute != null && (
            currentRoute == tab.route || 
            (tab.label == "Products" && currentRoute.startsWith("product_list")) ||
            (tab.label == "Orders" && currentRoute.startsWith("order_detail"))
        )
    }.coerceAtLeast(0)

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Background,
                tonalElevation = 0.dp,
                modifier = Modifier.drawBehind {
                    // Draw a 1dp top border instead of elevation shadow
                    drawLine(
                        color = Divider,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            ) {
                navTabs.forEachIndexed { index, tab ->
                    val selected = index == selectedIndex
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(tab.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (tab.label == "Alerts" && unreadCount > 0) {
                                        Badge(
                                            containerColor = StatusRed,
                                            contentColor = Color.White
                                        ) {
                                            Text(unreadCount.toString())
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.label,
                                    tint = if (selected) Accent else TextSecondary
                                )
                            }
                        },
                        label = {
                            Text(
                                text = tab.label,
                                color = if (selected) Accent else TextSecondary
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Accent,
                            unselectedIconColor = TextSecondary,
                            selectedTextColor = Accent,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = Background
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}
