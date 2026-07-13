package com.enesguneri.flickdrop.presentation.navigation

sealed class Screen(val route: String) {
    object Setup : Screen("setup_screen")
    object Discovery : Screen("discovery_screen/{displayName}") {
        // Radar ekranına geçerken kullanıcı adını parametre olarak ekleyen yardımcı fonksiyon
        fun createRoute(displayName: String): String {
            return "discovery_screen/$displayName"
        }
    }
}