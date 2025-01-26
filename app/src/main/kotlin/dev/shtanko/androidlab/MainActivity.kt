package dev.shtanko.androidlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import com.google.accompanist.adaptive.calculateDisplayFeatures
import dagger.hilt.android.AndroidEntryPoint
import dev.shtanko.androidlab.github.GithubApp
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme

@OptIn(ExperimentalComposeUiApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val displayFeatures = calculateDisplayFeatures(this)

            AndroidLabTheme {
                GithubApp(
                    modifier = Modifier.semantics { testTagsAsResourceId = true },
                    displayFeatures = displayFeatures,
                )
            }
        }
    }
}
