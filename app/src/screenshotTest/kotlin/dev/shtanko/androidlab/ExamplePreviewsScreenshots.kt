package dev.shtanko.androidlab

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme

class ExamplePreviewsScreenshots {
    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        AndroidLabTheme {
            Greeting("Android!")
        }
    }
}
