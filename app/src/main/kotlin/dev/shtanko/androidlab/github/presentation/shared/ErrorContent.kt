package dev.shtanko.androidlab.github.presentation.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.shtanko.androidlab.R
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme

@Composable
fun ErrorContent(
    modifier: Modifier = Modifier,
    onTryAgainClick: () -> Unit = {},
) {
    Box(
        modifier = modifier.fillMaxSize(),
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

@Preview(showBackground = true)
@Composable
private fun ErrorContentPreview() {
    AndroidLabTheme {
        ErrorContent()
    }
}
