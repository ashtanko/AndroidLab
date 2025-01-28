package dev.shtanko.androidlab.wifi.presentation

import android.app.Application
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.shtanko.androidlab.wifi.WifiCurrentConnectionResource
import dev.shtanko.androidlab.wifi.WifiScanResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class WiFiScannerViewModel(application: Application) : AndroidViewModel(application) {
    private val wifiManager: WifiManager =
        application.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager

    private val _wifiList = MutableStateFlow<ImmutableList<WifiScanResource>>(persistentListOf())
    val wifiList: StateFlow<ImmutableList<WifiScanResource>> = _wifiList

    private val _currentConnection = MutableStateFlow<WifiCurrentConnectionResource?>(null)
    val currentConnection: StateFlow<WifiCurrentConnectionResource?> = _currentConnection

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning

    fun onScanReceived() {
        val connectionInfo = wifiManager.connectionInfo
        _currentConnection.value = WifiCurrentConnectionResource(
            ssid = connectionInfo.ssid,
            rssi = connectionInfo.rssi,
            linkSpeed = connectionInfo.linkSpeed,
            frequency = connectionInfo.frequency,
        )

        _wifiList.value = wifiManager.scanResults.asSequence()
            .sortedBy { sr -> -sr.level }
            .map {
                WifiScanResource(
                    level = it.level,
                    wifiSsid = it.SSID.toString(),
                    frequency = it.frequency,
                    bssid = it.BSSID,
                )
            }.toImmutableList()
    }

    fun startScan() {
        viewModelScope.launch {
            while (true) {
                wifiManager.startScan()
                delay(1.seconds)
            }
        }
    }
}
