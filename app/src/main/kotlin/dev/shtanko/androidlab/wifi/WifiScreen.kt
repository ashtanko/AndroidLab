package dev.shtanko.androidlab.wifi

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.net.wifi.WifiManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import dev.shtanko.androidlab.github.designsystem.ScreenBackground
import dev.shtanko.androidlab.wifi.presentation.WiFiItem
import dev.shtanko.androidlab.wifi.presentation.WiFiScannerViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WifiScreen(
    modifier: Modifier = Modifier,
    viewModel: WiFiScannerViewModel = viewModel(),
) {

    val wifiList by viewModel.wifiList.collectAsState()
    val currentConnection = viewModel.currentConnection.collectAsState()

    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION,
    )

    val activity = LocalContext.current as Activity

    ScreenBackground(
        modifier = modifier.windowInsetsPadding(WindowInsets.navigationBars),
    ) {
        Scaffold(
            containerColor = Color.Transparent,
        ) { contentPadding ->
            Surface(modifier = Modifier.padding(contentPadding)) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    if (locationPermissionState.status.isGranted) {
                        val scanStared = remember { mutableStateOf(false) }

                        SystemBroadcastReceiver(
                            systemAction = WifiManager.SCAN_RESULTS_AVAILABLE_ACTION,
                            onSystemEvent = {
                                viewModel.onScanReceived()
                            },
                            startScan = {
                                viewModel.startScan()
                                scanStared.value = true
                            },
                        )

                        if (scanStared.value) {
                            WiFiScannerScreen(
                                wifiList = wifiList,
                                currentConnection = currentConnection,
                            )
                        }
                    } else {
                        Column {

                            val textToShow =
                                if (locationPermissionState.status.shouldShowRationale) {
                                    // If the user has denied the permission but the rationale can be shown,
                                    // then gently explain why the app requires this permission
                                    "The location is important for this app. Please grant the permission. ${locationPermissionState.status}"
                                } else {
                                    // If it's the first time the user lands on this feature, or the user
                                    // doesn't want to be asked again for this permission, explain that the
                                    // permission is required
                                    "Location permission required for this feature to be available. " +
                                        "Please grant the permission ${locationPermissionState.status}"
                                }

                            val buttonText =
                                if (locationPermissionState.status.shouldShowRationale) {
                                    "Request Permissions"
                                } else {
                                    "Open settings"
                                }

                            Text(
                                text = textToShow,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                            )
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                onClick = {
                                    if (locationPermissionState.status.shouldShowRationale) {
                                        locationPermissionState.launchPermissionRequest()
                                    } else {
                                        openAppSettings(activity)
                                    }
                                },
                            ) {
                                Text(
                                    modifier = Modifier,
                                    text = buttonText,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun openAppSettings(activity: Activity) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", activity.packageName, null),
    )
    activity.startActivity(intent)
}

@Immutable
data class WifiCurrentConnectionResource(
    val ssid: String,
    val rssi: Int?,
    val linkSpeed: Int,
    val frequency: Int,
)

@Composable
fun CurrentWiFiConnection(
    wifiInfoState: State<WifiCurrentConnectionResource?>,
    modifier: Modifier = Modifier,
) {
    val wifiInfo = wifiInfoState.value
    wifiInfo?.let {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Connected to: ${wifiInfo.ssid.trim('"')}",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = "Signal Strength: ${wifiInfo.rssi} dBm",
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(
                    text = "Link Speed: ${wifiInfo.linkSpeed} Mbps",
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(
                    text = "Frequency: ${wifiInfo.frequency} MHz",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Immutable
data class WifiScanResource(
    val level: Int,
    val wifiSsid: String?,
    val frequency: Int,
    val bssid: String,
)

@Composable
fun SystemBroadcastReceiver(
    systemAction: String,
    onSystemEvent: (intent: Intent?) -> Unit,
    startScan: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val currentOnSystemEvent by rememberUpdatedState(onSystemEvent)
    var scanStarted = remember { mutableStateOf(false) }

    DisposableEffect(context, systemAction) {
        val intentFilter = IntentFilter(systemAction)
        val broadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                currentOnSystemEvent(intent)
            }
        }

        context.registerReceiver(broadcast, intentFilter)

        onDispose {
            context.unregisterReceiver(broadcast)
        }
    }

    if (scanStarted.value == false) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                onClick = {
                    scanStarted.value = true
                    startScan()
                },
            ) {
                Text(
                    modifier = Modifier,
                    text = "Start scan",
                )
            }
        }
    }
}

@Composable
fun WiFiScannerScreen(
    wifiList: ImmutableList<WifiScanResource>,
    currentConnection: State<WifiCurrentConnectionResource?>,
    modifier: Modifier = Modifier,
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (isGranted) {
            // TODO
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text("Available WiFi Networks", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(8.dp))

        CurrentWiFiConnection(currentConnection)

        Spacer(modifier = Modifier.height(16.dp))

        WifiList(
            data = wifiList.toImmutableList(),
        )
    }
}

@Composable
fun WifiList(
    data: ImmutableList<WifiScanResource>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(
            data.size,
            key = {
                data[it].bssid
            },
            contentType = {
                data[it].wifiSsid
            },
        ) { i ->
            WiFiItem(
                frequency = data[i].frequency,
                wifiSsid = data[i].wifiSsid?.toString(),
                level = data[i].level,
            )
        }
    }
}
