package com.enesguneri.flickdrop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.enesguneri.flickdrop.presentation.discovery.DiscoveryScreen
import com.enesguneri.flickdrop.presentation.navigation.Screen
import com.enesguneri.flickdrop.presentation.setup.SetupScreen
import com.enesguneri.flickdrop.ui.theme.FlickDropTheme

class MainActivity : AppCompatActivity() {
    // AppCompat kullanılmasının nedeni dil değiştirme işleminin android 13'ten sonra
    // sadece ayarlardan değiştirilebilir hale gelmesiyle bizim manuel yapmak istememiz.
    // Bu işlemin de eski sürümlerle uyumlu olması için AppCompat yapılması gerekir.

    // Jetpack Compose'da Fragment'lar yerini @Composable fonksiyonlara bırakmıştır.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            setContent {
                FlickDropTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navController = rememberNavController()

                        NavHost(
                            navController = navController,
                            startDestination = Screen.Setup.route
                        ) {
                            // 1. EKRAN: Setup Ekranı
                            composable(route = Screen.Setup.route) {
                                SetupScreen(
                                    onNavigateToDiscovery = { displayName ->
                                        // Butona basıldığında ismi rotaya ekleyip Radar ekranına geç
                                        navController.navigate(Screen.Discovery.createRoute(displayName))
                                    }
                                )
                            }

                            // 2. EKRAN: Radar / Keşif Ekranı
                            composable(
                                route = Screen.Discovery.route,
                                arguments = listOf(
                                    navArgument("displayName") { type = NavType.StringType }
                                )
                            ) { backStackEntry ->
                                // Rotadaki {displayName} parametresini okuyup DiscoveryScreen'e veriyoruz
                                val displayName = backStackEntry.arguments?.getString("displayName") ?: "Anonym"
                                DiscoveryScreen(displayName = displayName)
                            }
                        }
                    }
                }
            }
        }
    }
}
