package com.enesguneri.flickdrop.data.network

import android.content.Context
import com.enesguneri.flickdrop.domain.model.FlickDropDevice
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionResolution
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.DiscoveryOptions
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import com.google.android.gms.nearby.connection.Strategy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NearbyController(private val context: Context) {

    private val connectionsClient = Nearby.getConnectionsClient(context)

    // P2P ağ yapısı için en uygun strateji: P2P_STAR veya P2P_CLUSTER.
    // P2P_STAR: Bir merkeze birden fazla cihazın bağlanabileceği yüksek hızlı transfer stratejisidir.
    private val strategy = Strategy.P2P_STAR

    // Uygulamamızın benzersiz servis kütüphane kimliği (Sadece aynı ID'ye sahip uygulamalar birbirini görür)
    private val serviceId = "com.enesguneri.flickdrop.P2P_NETWORK"

    // Keşfedilen (etraftaki) cihazları canlı olarak tuttuğumuz listemiz
    private val _discoveredDevices = MutableStateFlow<List<FlickDropDevice>>(emptyList())
    val discoveredDevices: StateFlow<List<FlickDropDevice>> = _discoveredDevices.asStateFlow()

    // 1. ADIM: AĞDA YAYIN YAPMA (ADVERTISING)
    // Cihaz kendi "Görünen Adını" etrafa duyurur
    fun startAdvertising(displayName: String) {
        val options = AdvertisingOptions.Builder().setStrategy(strategy).build()

        connectionsClient.startAdvertising(
            displayName,
            serviceId,
            connectionLifecycleCallback,
            options
        ).addOnSuccessListener {
            // Yayın başarılı bir şekilde başladı
        }.addOnFailureListener { exception ->
            // Yayın başlatılırken hata oluştu (örn: Bluetooth kapalı veya izin eksik)
        }
    }

    // 2. ADIM: ETRAFTAKİ CİHAZLARI TARAMA (DISCOVERY)
    // Etrafta yayın yapan diğer FlickDrop kullanıcılarını arar
    fun startDiscovery() {
        _discoveredDevices.value = emptyList() // Yeni taramada eski listeyi temizle
        val options = DiscoveryOptions.Builder().setStrategy(strategy).build()

        connectionsClient.startDiscovery(
            serviceId,
            endpointDiscoveryCallback,
            options
        ).addOnSuccessListener {
            // Tarama başarılı bir şekilde başladı
        }.addOnFailureListener { exception ->
            // Tarama başlatılamadı
        }
    }

    // Taramayı ve Yayını Durdurma
    fun stopAllEndpoints() {
        connectionsClient.stopAdvertising()
        connectionsClient.stopDiscovery()
        connectionsClient.stopAllEndpoints()
        _discoveredDevices.value = emptyList()
    }

    // --- GOOGLE API GERİ ÇAĞIRIM (CALLBACK) DİNLEYİCİLERİ ---

    // Etrafta bir cihaz bulunduğunda veya kaybolduğunda tetiklenen dinleyici
    private val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            // Sadece bizim servis kimliğimizle yayın yapan cihazları listeye ekle
            if (info.serviceId == serviceId) {
                val newDevice = FlickDropDevice(endpointId = endpointId, name = info.endpointName)
                _discoveredDevices.update { currentList ->
                    // Aynı cihaz zaten listede varsa tekrar eklememe kontrolü
                    if (currentList.none { it.endpointId == endpointId }) {
                        currentList + newDevice
                    } else {
                        currentList
                    }
                }
            }
        }

        override fun onEndpointLost(endpointId: String) {
            // Cihaz menzilden çıktığında veya uygulamasını kapattığında listeden çıkar
            _discoveredDevices.update { currentList ->
                currentList.filterNot { it.endpointId == endpointId }
            }
        }
    }

    // Bağlantı isteklerini ve durumlarını yöneten dinleyici (Sprint 3'te içini dolduracağız)
    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            // Bir cihaz bağlanmak istediğinde tetiklenir
        }

        override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
            // Bağlantının kabul edilip edilmediği sonucunu verir
        }

        override fun onDisconnected(endpointId: String) {
            // Bağlantı koptuğunda tetiklenir
        }
    }
}