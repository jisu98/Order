package com.jisu98.order

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jisu98.order.presentation.menu.MenuScreen
import com.jisu98.order.presentation.order.OrderScreen
import com.jisu98.order.ui.theme.OrderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OrderTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "menu",
                        modifier = Modifier.padding(innerPadding),
                    ) {
                        composable("menu") {
                            MenuScreen(
                                onNavigateToOrder = { navController.navigate("order") },
                            )
                        }
                        composable("order") {
                            OrderScreen(
                                onOrderComplete = { navController.popBackStack() },
                            )
                        }
                    }
                }
            }
        }
    }
}
