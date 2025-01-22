package dev.shtanko.androidlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import dev.shtanko.androidlab.github.presentation.UsersScreen
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidLabTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    UsersScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
