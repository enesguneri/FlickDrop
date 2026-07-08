package com.enesguneri.flickdrop.presentation.setup

// Ekrandaki tüm UI elemanlarının durumunu tutan tek kaynak
data class SetupState(
    val displayName: String = "",
    val isButtonEnabled: Boolean = false
)