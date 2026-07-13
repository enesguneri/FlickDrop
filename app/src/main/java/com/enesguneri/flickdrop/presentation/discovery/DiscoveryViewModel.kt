package com.enesguneri.flickdrop.presentation.discovery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.enesguneri.flickdrop.data.network.NearbyController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class DiscoveryViewModel(application: Application) : AndroidViewModel(application) {

    // NearbyController'ı uygulama context'i ile başlatıyoruz
    private val nearbyController = NearbyController(application.applicationContext)

    private val _state = MutableStateFlow(DiscoveryState())
    val state: StateFlow<DiscoveryState> = _state.asStateFlow()

    init {
        // NearbyController'daki canlı cihaz listesini (StateFlow) dinle ve bizim UI State'imize aktar
        nearbyController.discoveredDevices
            .onEach { devices ->
                _state.update { it.copy(discoveredDevices = devices) }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: DiscoveryEvent) {
        when (event) {
            is DiscoveryEvent.OnPermissionsResult -> {
                _state.update { it.copy(arePermissionsGranted = event.isGranted) }
                // İzinler verildiyse otomatik olarak taramayı ve yayını başlat
                if (event.isGranted) {
                    startNetworkOperations()
                }
            }
            DiscoveryEvent.StartScanning -> {
                startNetworkOperations()
            }
            DiscoveryEvent.StopScanning -> {
                nearbyController.stopAllEndpoints()
                _state.update { it.copy(isScanning = false) }
            }
            is DiscoveryEvent.OnDeviceSelected -> {
                // Sprint 3'te bir cihaza tıklandığında bağlantı (Handshake) isteği göndereceğiz
            }
        }
    }

    // Setup ekranından gelen kullanıcı adını State'e kaydet
    fun setDisplayName(name: String) {
        _state.update { it.copy(myDisplayName = name) }
    }

    private fun startNetworkOperations() {
        if (_state.value.arePermissionsGranted && _state.value.myDisplayName.isNotEmpty()) {
            _state.update { it.copy(isScanning = true) }
            // Hem etrafa "Ben buradayım" diye yayın yapıyoruz...
            nearbyController.startAdvertising(_state.value.myDisplayName)
            // ...Hem de etraftaki diğer FlickDrop cihazlarını arıyoruz!
            nearbyController.startDiscovery()
        }
    }

    override fun onCleared() {
        super.onCleared()
        // ViewModel yok olduğunda (ekran kapandığında) pil tüketmemek için tüm antenleri kapat
        nearbyController.stopAllEndpoints()
    }
}