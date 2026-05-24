package com.hiranya.printxpress.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hiranya.printxpress.ui.Screen
import com.hiranya.printxpress.ui.theme.Accent
import com.hiranya.printxpress.ui.theme.Background
import com.hiranya.printxpress.ui.theme.Divider
import com.hiranya.printxpress.ui.theme.TextSecondary

// Tab definition for the bottom navigation bar
private data class NavTab(
    val label: String,
    val icon: ImageVector,
    val route: String
)

private val navTabs = listOf(
    NavTab("Home", Icons.Rounded.Home, Screen.Home.route),
    NavTab("Products", Icons.Rounded.GridView, Screen.ProductList.withId(0)),
    NavTab("Orders", Icons.Rounded.Receipt, Screen.Orders.route),
    NavTab("Alerts", Icons.Rounded.Notifications, Screen.Notifications.route),
    NavTab("Profile", Icons.Rounded.Person, Screen.Profile.route)
)

// Wraps any bottom-nav screen with the shared navigation bar and scaffold
@Composable
fun MainScaffold(navController: NavController, content: @Composable (PaddingValues) -> Unit) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Determine which tab is active by matching the current route
    val selectedIndex = navTabs.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)

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
                                popUpTo(Screen.Home.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.label,
                                tint = if (selected) Accent else TextSecondary
                            )
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
