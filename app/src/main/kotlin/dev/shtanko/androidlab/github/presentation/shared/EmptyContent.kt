package dev.shtanko.androidlab.github.presentation.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme

@Composable
fun EmptyContent(
    content: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier
                .padding(all = 32.dp)
                .testTag("EmptyUsersContent"),
            text = content,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyContentPreview() {
    AndroidLabTheme {
        EmptyContent(content = "Empty content")
    }
}
