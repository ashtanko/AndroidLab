package dev.shtanko.androidlab.github.presentation.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.shtanko.androidlab.ui.shared.CircularAvatarImage
import dev.shtanko.androidlab.ui.shared.EmptyContent
import dev.shtanko.androidlab.ui.shared.ErrorContent
import dev.shtanko.androidlab.ui.shared.ItemLoading
import dev.shtanko.androidlab.ui.shared.ItemNoMoreItems
import dev.shtanko.androidlab.ui.shared.LoadingContent
import dev.shtanko.androidlab.ui.shared.PullToRefresh
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import dev.shtanko.androidlab.utils.ThemePreviews

class SharedPreviewsScreenshots {
    @ThemePreviews
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

    @ThemePreviews
    @Composable
    private fun RepositoryNoMoreItemsPreview() {
        AndroidLabTheme {
            ItemNoMoreItems()
        }
    }

    @ThemePreviews
    @Composable
    private fun LoadingContentPreview() {
        AndroidLabTheme {
            LoadingContent(
                testTag = "test_loading",
            )
        }
    }

    @ThemePreviews
    @Composable
    private fun ItemLoadingPreview() {
        AndroidLabTheme {
            ItemLoading()
        }
    }

    @ThemePreviews
    @Composable
    private fun ErrorContentPreview() {
        AndroidLabTheme {
            ErrorContent()
        }
    }

    @ThemePreviews
    @Composable
    private fun EmptyContentPreview() {
        AndroidLabTheme {
            EmptyContent(
                content = "Empty content",
                testTag = "test_empty",
            )
        }
    }

    @ThemePreviews
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
