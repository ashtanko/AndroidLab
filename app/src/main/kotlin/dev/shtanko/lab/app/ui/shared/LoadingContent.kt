package dev.shtanko.lab.app.ui.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.shtanko.androidlab.core.designsystem.theme.AndroidLabTheme
import dev.shtanko.lab.app.utils.ThemePreviews

@Composable
@NonRestartableComposable
fun LoadingContent(
    testTag: String,
    modifier: Modifier = Modifier,
    circularProgressSize: Dp = 32.dp,
) {
    Surface(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(size = circularProgressSize)
                    .testTag(testTag),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeWidth = 4.dp,
            )
        }
    }
}

@ThemePreviews
@Composable
private fun LoadingContentPreview() {
    AndroidLabTheme {
        LoadingContent(
            testTag = "LoadingContent",
        )
    }
}
