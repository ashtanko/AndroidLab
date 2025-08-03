@file:OptIn(ExperimentalMaterial3Api::class)

package dev.shtanko.lab.app.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@AndroidEntryPoint
class BleActivity : ComponentActivity() {
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                RequestBluetoothPermissions {
                    // todo
                }
            }
        }
    }
}

@Composable
@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: BluetoothScanViewModel = viewModel(),
) {
    val devices = viewModel.devices.collectAsState().value
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bluetooth Devices") },
                actions = {
                    IconButton(onClick = { viewModel.startScan() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Scan")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize(),
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(devices.size) { index ->
                    val device = devices[index]
                    DeviceItem(device)
                }
            }
        }

        DisposableEffect(Unit) {
            viewModel.startScan()
            onDispose { viewModel.stopScan() }
        }
    }
}

@SuppressLint("MissingPermission") // Ensure permissions are checked elsewhere
class BluetoothScanViewModel : ViewModel() {
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val _devices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val devices: StateFlow<List<BluetoothDevice>> = _devices

    private val leScanner = bluetoothAdapter?.bluetoothLeScanner
    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            if (device.name != null && device !in _devices.value) {
                _devices.value += device
            }
        }
    }

    fun startScan() {
        leScanner?.startScan(scanCallback)
    }

    fun stopScan() {
        leScanner?.stopScan(scanCallback)
    }
}

@Composable
@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
fun DeviceItem(
    device: BluetoothDevice,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { /* Handle device click */ }
            .padding(16.dp),
    ) {
        Text(text = device.name ?: "Unknown Device", style = MaterialTheme.typography.bodyLarge)
        Text(text = device.address, style = MaterialTheme.typography.bodySmall)
    }
}

@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
@Composable
fun RequestBluetoothPermissions(onPermissionsGranted: () -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions.all { it.value }) {
                onPermissionsGranted()
            }
        },
    )

    MainScreen()

    LaunchedEffect(Unit) {
        launcher.launch(
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
            ),
        )
    }
}
