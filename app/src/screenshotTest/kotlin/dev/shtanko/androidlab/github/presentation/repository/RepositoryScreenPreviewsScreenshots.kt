package dev.shtanko.androidlab.github.presentation.repository

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import dev.shtanko.androidlab.github.presentation.model.RepositoryResource
import dev.shtanko.androidlab.github.presentation.preview.RepositoriesDataProvider
import dev.shtanko.androidlab.github.presentation.repositories.RepositoriesScreen
import dev.shtanko.androidlab.github.presentation.repositories.RepositoriesTopAppBar
import dev.shtanko.androidlab.github.presentation.repositories.RepositoryItem
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.MutableStateFlow

class RepositoryScreenPreviewsScreenshots {
    @Preview(
        name = "Light Mode",
        showBackground = true,
        uiMode = Configuration.UI_MODE_NIGHT_NO,
    )
    @Preview(
        name = "Dark Mode",
        showBackground = true,
        uiMode = Configuration.UI_MODE_NIGHT_YES,
    )
    @Composable
    private fun RepositoriesTopAppBarPreview() {
        AndroidLabTheme {
            RepositoriesTopAppBar()
        }
    }

    @Preview(
        name = "Light Mode",
        showBackground = true,
        uiMode = Configuration.UI_MODE_NIGHT_NO,
    )
    @Preview(
        name = "Dark Mode",
        showBackground = true,
        uiMode = Configuration.UI_MODE_NIGHT_YES,
    )
    @Composable
    private fun RepositoriesScreenItemsPreview(
        @PreviewParameter(RepositoriesDataProvider::class) preview: ImmutableList<RepositoryResource>,
    ) {
        AndroidLabTheme {
            val pagingData = PagingData.from(preview)
            val flow = MutableStateFlow(pagingData)
            RepositoriesScreen(
                uiState = flow.collectAsLazyPagingItems(),
                isRefreshing = false,
            )
        }
    }

    @Preview(
        name = "Light Mode",
        showBackground = true,
        uiMode = Configuration.UI_MODE_NIGHT_NO,
    )
    @Preview(
        name = "Dark Mode",
        showBackground = true,
        uiMode = Configuration.UI_MODE_NIGHT_YES,
    )
    @Composable
    private fun RepositoryItemPreview(
        @PreviewParameter(RepositoriesDataProvider::class) preview: ImmutableList<RepositoryResource>,
    ) {
        AndroidLabTheme {
            RepositoryItem(item = preview.first())
        }
    }
}
