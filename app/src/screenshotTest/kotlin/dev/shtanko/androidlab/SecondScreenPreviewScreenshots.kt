package dev.shtanko.androidlab

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme

class SecondScreenPreviewScreenshots {
    @Preview(showBackground = true)
    @Composable
    fun SecondScreenPreview() {
        AndroidLabTheme {
            SecondScreen(onNavigateToNextScreen = {})
        }
    }
}
