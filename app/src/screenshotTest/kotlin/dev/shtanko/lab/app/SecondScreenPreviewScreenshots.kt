package dev.shtanko.lab.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.shtanko.androidlab.core.designsystem.theme.AndroidLabTheme

class SecondScreenPreviewScreenshots {
    @Preview(showBackground = true)
    @Composable
    fun SecondScreenPreview() {
        AndroidLabTheme {
            SecondScreen(onNavigateToNextScreen = {})
        }
    }
}
