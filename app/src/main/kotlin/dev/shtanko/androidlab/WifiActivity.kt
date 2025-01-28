package dev.shtanko.androidlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import dagger.hilt.android.AndroidEntryPoint
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import dev.shtanko.androidlab.wifi.WifiApp

@OptIn(ExperimentalComposeUiApi::class)
@AndroidEntryPoint
class WifiActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidLabTheme {
                WifiApp(
                    modifier = Modifier.semantics { testTagsAsResourceId = true },
                )
            }
        }
    }
}
