package com.enesguneri.flickdrop.presentation.discovery

import com.enesguneri.flickdrop.domain.model.FlickDropDevice

sealed class DiscoveryEvent {
    object StartScanning : DiscoveryEvent()
    object StopScanning : DiscoveryEvent()
    data class OnPermissionsResult(val isGranted: Boolean) : DiscoveryEvent()
    data class OnDeviceSelected(val device: FlickDropDevice) : DiscoveryEvent()
}