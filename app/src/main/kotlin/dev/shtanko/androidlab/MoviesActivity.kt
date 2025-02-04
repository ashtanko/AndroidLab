package dev.shtanko.androidlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import com.google.accompanist.adaptive.calculateDisplayFeatures
import dagger.hilt.android.AndroidEntryPoint
import dev.shtanko.androidlab.movies.MoviesApp
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MoviesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val displayFeatures = calculateDisplayFeatures(this)
            val modifier = Modifier.semantics { testTagsAsResourceId = true }
            val windowSize = calculateWindowSizeClass(this)

            AndroidLabTheme {
                MoviesApp(
                    windowSize = windowSize,
                    displayFeatures = displayFeatures,
                    modifier = modifier,
                )
            }
        }
    }
}
