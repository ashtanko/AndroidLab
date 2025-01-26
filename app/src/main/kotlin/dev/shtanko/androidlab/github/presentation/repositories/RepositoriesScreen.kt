@file:OptIn(ExperimentalMaterial3Api::class)

package dev.shtanko.androidlab.github.presentation.repositories

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import dev.shtanko.androidlab.github.presentation.model.RepositoryResource
import dev.shtanko.androidlab.github.presentation.preview.RepositoriesDataProvider
import dev.shtanko.androidlab.github.presentation.shared.CircularAvatarImage
import dev.shtanko.androidlab.github.presentation.shared.PullToRefresh
import dev.shtanko.androidlab.github.presentation.shared.ScreenBackground
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.flowOf

@Composable
fun RepositoriesScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
    viewModel: RepositoriesViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsLazyPagingItems()

    ScreenBackground(
        modifier = modifier.windowInsetsPadding(WindowInsets.navigationBars),
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                RepositoriesTopAppBar(
                    navigateBack = navigateBack,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            containerColor = Color.Transparent,
        ) { contentPadding ->
            RepositoriesScreen(
                modifier = Modifier.padding(contentPadding),
                uiState = uiState,
                isRefreshing = false,
                onRefresh = { viewModel.refresh() },
            )
        }
    }
}

@Composable
fun RepositoriesScreen(
    uiState: LazyPagingItems<RepositoryResource>,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = true,
    onTryAgainClick: () -> Unit = {},
    onClick: (Int) -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    RepositoriesList(
        pagingItems = uiState,
        isRefreshing = isRefreshing,
        modifier = modifier,
        onClick = onClick,
        onRefresh = onRefresh,
    )
}

@Composable
private fun RepositoriesList(
    pagingItems: LazyPagingItems<RepositoryResource>,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = true,
    onClick: (Int) -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = 0,
    )
    PullToRefresh(
        modifier = modifier,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        testTag = "RepositoriesPullToRefreshBox",
        testIndicatorTag = "RepositoriesPullToRefreshIndicator",
        content = {
            LazyColumn(
                state = listState,
                modifier = Modifier,
                content = {
                    items(
                        count = pagingItems.itemCount,
                        key = pagingItems.itemKey { it.id },
                        contentType = pagingItems.itemContentType { "Item" },
                    ) { i ->

                        val item = pagingItems[i]
                        item?.let {
                            RepositoryItem(
                                item = it,
                                onClick = onClick,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                },
            )
        },
    )
}

@Composable
fun RepositoryItem(
    item: RepositoryResource,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit = {},
) {
    Box(modifier = modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceContainer,
            onClick = {
                onClick(item.id)
            },
            modifier = Modifier,
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CircularAvatarImage(
                        sizeDp = 16.dp,
                        borderDp = 0.dp,
                        imageUrl = item.owner?.avatarUrl,
                    )
                    Text(
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(start = 8.dp),
                        text = item.owner?.login.orEmpty(),
                    )
                }
                Row(
                    modifier = Modifier.padding(bottom = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier,
                        text = item.name.orEmpty(),
                    )
                }
                item.description.takeIf { it?.isNotEmpty() == true }?.let { description ->
                    Text(
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 2.dp),
                        text = description,
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.Rounded.Star,
                            contentDescription = null,
                        )
                        Text(
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier,
                            text = item.stars.toString(),
                        )
                    }
                    Text(
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp),
                        text = item.language.orEmpty(),
                    )
                }
            }
        }
    }
}

@Composable
fun RepositoriesTopAppBar(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = navigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                )
            }
        },
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun RepositoriesTopAppBarPreview() {
    AndroidLabTheme {
        RepositoriesTopAppBar()
    }
}

@Preview(showBackground = true)
@Composable
private fun RepositoriesScreenItemsPreview(
    @PreviewParameter(RepositoriesDataProvider::class) preview: ImmutableList<RepositoryResource>,
) {
    AndroidLabTheme {
        val pagingData = PagingData.from(preview)
        RepositoriesScreen(
            uiState = flowOf(pagingData).collectAsLazyPagingItems(),
            isRefreshing = false,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RepositoryItemPreview(
    @PreviewParameter(RepositoriesDataProvider::class) preview: ImmutableList<RepositoryResource>,
) {
    AndroidLabTheme {
        RepositoryItem(item = preview.first())
    }
}
