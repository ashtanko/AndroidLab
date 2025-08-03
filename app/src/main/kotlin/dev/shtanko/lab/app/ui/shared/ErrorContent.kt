package dev.shtanko.lab.app.ui.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import dev.shtanko.androidlab.core.designsystem.theme.AndroidLabTheme
import dev.shtanko.lab.app.R
import dev.shtanko.lab.app.utils.ThemePreviews

@Composable
@NonRestartableComposable
fun ErrorContent(
    modifier: Modifier = Modifier,
    onTryAgainClick: () -> Unit = {},
) {
    Surface(modifier = modifier) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Button(
                modifier = Modifier.testTag("tryAgainButton"),
                onClick = onTryAgainClick,
            ) {
                Text(
                    modifier = Modifier,
                    text = stringResource(R.string.try_again),
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun ErrorContentPreview() {
    AndroidLabTheme {
        ErrorContent()
    }
}
