package dev.shtanko.lab.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.shtanko.androidlab.core.designsystem.theme.AndroidLabTheme

class FirstScreenPreviewScreenshots {
    @Preview(showBackground = true)
    @Composable
    fun FirstScreenPreview() {
        AndroidLabTheme {
            FirstScreen(onNavigateToNextScreen = {})
        }
    }
}
