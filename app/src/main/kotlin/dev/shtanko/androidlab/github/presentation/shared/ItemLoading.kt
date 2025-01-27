package dev.shtanko.androidlab.github.presentation.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import dev.shtanko.androidlab.utils.ThemesPreviews

@Composable
fun ItemLoading(
    modifier: Modifier = Modifier,
    circularProgressSize: Dp = 22.dp,
) {
    Surface(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(size = circularProgressSize),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}

@ThemesPreviews
@Composable
private fun ItemLoadingPreview() {
    AndroidLabTheme {
        ItemLoading()
    }
}
