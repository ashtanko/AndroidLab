package dev.shtanko.lab.app.wifi.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.shtanko.androidlab.core.designsystem.theme.AndroidLabTheme
import dev.shtanko.lab.app.R
import dev.shtanko.lab.app.utils.ThemePreviews

@Composable
fun WiFiItem(
    level: Int,
    wifiSsid: String?,
    frequency: Int,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = wifiSsid.orEmpty(), style = MaterialTheme.typography.bodyLarge)
            Text(
                text = stringResource(R.string.wifi_level, level),
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = stringResource(R.string.wifi_frequency, frequency),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@ThemePreviews
@Composable
private fun WiFiItemPreview() {
    AndroidLabTheme {
        WiFiItem(
            wifiSsid = "Sample WiFi",
            level = -50,
            frequency = 2412,
        )
    }
}
