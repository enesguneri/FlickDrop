package com.enesguneri.flickdrop.presentation.setup

// Kullanıcının Setup ekranında yapabileceği eylemler
sealed class SetupEvent {
    data class OnNameChanged(val name: String) : SetupEvent()
    data class OnLanguageChanged(val languageCode: String) : SetupEvent()
    object OnStartClicked : SetupEvent()
}