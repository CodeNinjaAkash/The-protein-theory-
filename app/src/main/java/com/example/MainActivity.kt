package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.PtTheme
import com.example.viewmodel.PtViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PtTheme {
                val viewModel: PtViewModel = viewModel()
                var currentTab by remember { mutableStateOf("home") }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar(
                            containerColor = Color(0xFF161619),
                            contentColor = Color.White,
                            modifier = Modifier.testTag("app_navigation_bar")
                        ) {
                            NavigationBarItem(
                                selected = currentTab == "home",
                                onClick = { currentTab = "home" },
                                icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = if (currentTab == "home") Color(0xFFFF1E27) else Color.Gray) },
                                label = { Text("Home", fontSize = 10.sp, color = if (currentTab == "home") Color.White else Color.Gray) },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = Color(0xFFFF1E27).copy(alpha = 0.15f)
                                )
                            )
                            NavigationBarItem(
                                selected = currentTab == "menu",
                                onClick = { currentTab = "menu" },
                                icon = { Icon(Icons.Default.RestaurantMenu, contentDescription = "Menu", tint = if (currentTab == "menu") Color(0xFFFF1E27) else Color.Gray) },
                                label = { Text("Menu", fontSize = 10.sp, color = if (currentTab == "menu") Color.White else Color.Gray) },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = Color(0xFFFF1E27).copy(alpha = 0.15f)
                                )
                            )
                            NavigationBarItem(
                                selected = currentTab == "cart",
                                onClick = { currentTab = "cart" },
                                icon = { Icon(Icons.Default.ShoppingBasket, contentDescription = "Cart", tint = if (currentTab == "cart") Color(0xFFFF1E27) else Color.Gray) },
                                label = { Text("Cart", fontSize = 10.sp, color = if (currentTab == "cart") Color.White else Color.Gray) },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = Color(0xFFFF1E27).copy(alpha = 0.15f)
                                )
                            )
                            NavigationBarItem(
                                selected = currentTab == "coaching",
                                onClick = { currentTab = "coaching" },
                                icon = { Icon(Icons.Default.FitnessCenter, contentDescription = "Coaching", tint = if (currentTab == "coaching") Color(0xFFFF1E27) else Color.Gray) },
                                label = { Text("Coach", fontSize = 10.sp, color = if (currentTab == "coaching") Color.White else Color.Gray) },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = Color(0xFFFF1E27).copy(alpha = 0.15f)
                                )
                            )
                            NavigationBarItem(
                                selected = currentTab == "loyalty",
                                onClick = { currentTab = "loyalty" },
                                icon = { Icon(Icons.Default.CardMembership, contentDescription = "Loyalty", tint = if (currentTab == "loyalty") Color(0xFFFF1E27) else Color.Gray) },
                                label = { Text("Awards", fontSize = 10.sp, color = if (currentTab == "loyalty") Color.White else Color.Gray) },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = Color(0xFFFF1E27).copy(alpha = 0.15f)
                                )
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentTab) {
                            "home" -> DashboardScreen(
                                viewModel = viewModel,
                                onNavigateToMenu = { currentTab = "menu" },
                                onNavigateToCoaching = { currentTab = "coaching" },
                                onNavigateToContact = { currentTab = "contact" },
                                onNavigateToBilling = { currentTab = "billing" }
                            )
                            "menu" -> MenuScreen(
                                viewModel = viewModel,
                                onNavigateToCart = { currentTab = "cart" }
                            )
                            "cart" -> CartScreen(
                                viewModel = viewModel,
                                onNavigateToMenu = { currentTab = "menu" }
                            )
                            "coaching" -> CoachingScreen(
                                viewModel = viewModel
                            )
                            "loyalty" -> RewardsScreen(
                                viewModel = viewModel
                            )
                            "contact" -> ContactScreen(
                                viewModel = viewModel
                            )
                            "billing" -> BillingScreen(
                                viewModel = viewModel,
                                onBackToHome = { currentTab = "home" }
                            )
                        }
                    }
                }
            }
        }
    }
}
