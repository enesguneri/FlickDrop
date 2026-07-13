package com.enesguneri.flickdrop.presentation.discovery

import com.enesguneri.flickdrop.domain.model.FlickDropDevice

data class DiscoveryState(
    val myDisplayName: String = "",
    val discoveredDevices: List<FlickDropDevice> = emptyList(),
    val isScanning: Boolean = false,
    val arePermissionsGranted: Boolean = false
)