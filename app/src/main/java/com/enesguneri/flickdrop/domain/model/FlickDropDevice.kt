package com.enesguneri.flickdrop.domain.model

// Ağda keşfedilen veya bize bağlanan bir cihazı temsil eder
data class FlickDropDevice(
    val endpointId: String, // Google Nearby API'nin o anki oturum için verdiği benzersiz ID
    val name: String        // Karşı kullanıcının Setup ekranında belirlediği Görünen Ad
)