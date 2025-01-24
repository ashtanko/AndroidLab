package dev.shtanko.androidlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import dagger.hilt.android.AndroidEntryPoint
import dev.shtanko.androidlab.github.presentation.users.UsersScreen
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme

@OptIn(ExperimentalComposeUiApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidLabTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .semantics { testTagsAsResourceId = true }) { innerPadding ->
                    UsersScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
