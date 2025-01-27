package dev.shtanko.androidlab.github.presentation.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import dev.shtanko.androidlab.utils.ThemesPreviews

class SharedPreviewsScreenshots {
    @ThemesPreviews
    @Composable
    private fun PullToRefreshPreview() {
        AndroidLabTheme {
            PullToRefresh(
                content = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Text(
                            text = "Pull to refresh",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                },
            )
        }
    }

    @ThemesPreviews
    @Composable
    private fun RepositoryNoMoreItemsPreview() {
        AndroidLabTheme {
            ItemNoMoreItems()
        }
    }

    @ThemesPreviews
    @Composable
    private fun LoadingContentPreview() {
        AndroidLabTheme {
            LoadingContent()
        }
    }

    @ThemesPreviews
    @Composable
    private fun ItemLoadingPreview() {
        AndroidLabTheme {
            ItemLoading()
        }
    }

    @ThemesPreviews
    @Composable
    private fun ErrorContentPreview() {
        AndroidLabTheme {
            ErrorContent()
        }
    }

    @ThemesPreviews
    @Composable
    private fun EmptyContentPreview() {
        AndroidLabTheme {
            EmptyContent(content = "Empty content")
        }
    }

    @ThemesPreviews
    @Composable
    private fun CircularAvatarImagePreview() {
        AndroidLabTheme {
            CircularAvatarImage(
                sizeDp = 32.dp,
                imageUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
            )
        }
    }
}
