package com.enesguneri.flickdrop.presentation.setup

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SetupViewModel : ViewModel() {

    // UI'ın dinleyeceği durum değişkeni (Sadece ViewModel değiştirebilir)
    private val _state = MutableStateFlow(SetupState())
    val state: StateFlow<SetupState> = _state.asStateFlow()

    // Arayüzden (UI) gelen eylemleri (Intent/Event) yakalayan fonksiyon
    fun onEvent(event: SetupEvent) {
        when (event) {
            is SetupEvent.OnNameChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        displayName = event.name,
                        // İsim en az 3 karakterse butonu aktif et (Basit form doğrulama)
                        isButtonEnabled = event.name.trim().length >= 3
                    )
                }
            }
            is SetupEvent.OnLanguageChanged -> {
                // AndroidX AppCompat kullanarak uygulama dilini anında değiştir ve kaydet
                val appLocale = LocaleListCompat.forLanguageTags(event.languageCode)
                AppCompatDelegate.setApplicationLocales(appLocale)
            }
            SetupEvent.OnStartClicked -> {
                // Şimdilik boş. Bir sonraki adımda Navigation ile diğer ekrana geçeceğiz.
            }
        }
    }
}