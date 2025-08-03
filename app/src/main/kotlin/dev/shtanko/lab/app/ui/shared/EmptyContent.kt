package dev.shtanko.lab.app.ui.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import dev.shtanko.androidlab.core.designsystem.theme.AndroidLabTheme
import dev.shtanko.lab.app.utils.ThemePreviews

@Composable
@NonRestartableComposable
fun EmptyContent(
    content: String,
    testTag: String,
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier = Modifier
                    .padding(all = 32.dp)
                    .testTag(testTag),
                text = content,
            )
        }
    }
}

@ThemePreviews
@Composable
private fun EmptyContentPreview() {
    AndroidLabTheme {
        EmptyContent(
            content = "Empty content",
            testTag = "EmptyContent",
        )
    }
}
