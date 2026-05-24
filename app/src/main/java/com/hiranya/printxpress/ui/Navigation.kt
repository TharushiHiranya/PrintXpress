package com.hiranya.printxpress.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hiranya.printxpress.ui.screens.AddressesScreen
import com.hiranya.printxpress.ui.screens.EditProfileScreen
import com.hiranya.printxpress.ui.screens.FaqScreen
import com.hiranya.printxpress.ui.screens.HomeScreen
import com.hiranya.printxpress.ui.screens.LoginScreen
import com.hiranya.printxpress.ui.screens.NotificationsScreen
import com.hiranya.printxpress.ui.screens.OrderConfirmationScreen
import com.hiranya.printxpress.ui.screens.OrderDetailScreen
import com.hiranya.printxpress.ui.screens.OrdersScreen
import com.hiranya.printxpress.ui.screens.PlaceOrderScreen
import com.hiranya.printxpress.ui.screens.PrintGuidelinesScreen
import com.hiranya.printxpress.ui.screens.ProductDetailScreen
import com.hiranya.printxpress.ui.screens.ProductListScreen
import com.hiranya.printxpress.ui.screens.ProfileScreen
import com.hiranya.printxpress.ui.screens.RegisterScreen
import com.hiranya.printxpress.ui.screens.SplashScreen

// One route string per screen. Screens that receive an ID append "/{id}" to their base route.
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object ProductList : Screen("product_list/{categoryId}") {
        fun withId(categoryId: Long) = "product_list/$categoryId"
    }
    object ProductDetail : Screen("product_detail/{productId}") {
        fun withId(productId: Long) = "product_detail/$productId"
    }
    object PlaceOrder : Screen("place_order/{productId}") {
        fun withId(productId: Long) = "place_order/$productId"
    }
    object OrderConfirmation : Screen("order_confirmation/{orderId}") {
        fun withId(orderId: Long) = "order_confirmation/$orderId"
    }
    object Orders : Screen("orders")
    object OrderDetail : Screen("order_detail/{orderId}") {
        fun withId(orderId: Long) = "order_detail/$orderId"
    }
    object Notifications : Screen("notifications")
    object Profile : Screen("profile")
    object EditProfile : Screen("edit_profile")
    object Addresses : Screen("addresses")
    object PrintGuidelines : Screen("print_guidelines")
    object Faq : Screen("faq")
}

// Root navigation graph. Set up once in MainActivity and never recreated.
@Composable
fun PrintXpressNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) { SplashScreen(navController) }
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Register.route) { RegisterScreen(navController) }
        composable(Screen.Home.route) { HomeScreen(navController) }

        composable(
            route = Screen.ProductList.route,
            arguments = listOf(navArgument("categoryId") { type = NavType.LongType })
        ) { backStackEntry ->
            ProductListScreen(
                navController = navController,
                categoryId = backStackEntry.arguments!!.getLong("categoryId")
            )
        }

        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.LongType })
        ) { backStackEntry ->
            ProductDetailScreen(
                navController = navController,
                productId = backStackEntry.arguments!!.getLong("productId")
            )
        }

        composable(
            route = Screen.PlaceOrder.route,
            arguments = listOf(navArgument("productId") { type = NavType.LongType })
        ) { backStackEntry ->
            PlaceOrderScreen(
                navController = navController,
                productId = backStackEntry.arguments!!.getLong("productId")
            )
        }

        composable(
            route = Screen.OrderConfirmation.route,
            arguments = listOf(navArgument("orderId") { type = NavType.LongType })
        ) { backStackEntry ->
            OrderConfirmationScreen(
                navController = navController,
                orderId = backStackEntry.arguments!!.getLong("orderId")
            )
        }

        composable(Screen.Orders.route) { OrdersScreen(navController) }

        composable(
            route = Screen.OrderDetail.route,
            arguments = listOf(navArgument("orderId") { type = NavType.LongType })
        ) { backStackEntry ->
            OrderDetailScreen(
                navController = navController,
                orderId = backStackEntry.arguments!!.getLong("orderId")
            )
        }

        composable(Screen.Notifications.route) { NotificationsScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }
        composable(Screen.EditProfile.route) { EditProfileScreen(navController) }
        composable(Screen.Addresses.route) { AddressesScreen(navController) }
        composable(Screen.PrintGuidelines.route) { PrintGuidelinesScreen(navController) }
        composable(Screen.Faq.route) { FaqScreen(navController) }
    }
}
