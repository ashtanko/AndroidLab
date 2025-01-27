package dev.shtanko.androidlab.github.presentation.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.shtanko.androidlab.R
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import dev.shtanko.androidlab.utils.ThemesPreviews

@Composable
fun ItemNoMoreItems(
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                maxLines = 1,
                style = MaterialTheme.typography.labelLarge,
                text = stringResource(R.string.end_of_list_title),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@ThemesPreviews
@Composable
private fun RepositoryNoMoreItemsPreview() {
    AndroidLabTheme {
        ItemNoMoreItems()
    }
}
