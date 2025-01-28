@file:Suppress("TooManyFunctions")
@file:OptIn(ExperimentalMaterial3Api::class)

package dev.shtanko.androidlab.github.presentation.repositories

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import dev.shtanko.androidlab.R
import dev.shtanko.androidlab.github.designsystem.ScreenBackground
import dev.shtanko.androidlab.github.presentation.model.RepositoryResource
import dev.shtanko.androidlab.github.presentation.model.UserFullResource
import dev.shtanko.androidlab.github.presentation.preview.RepositoriesDataProvider
import dev.shtanko.androidlab.github.presentation.shared.EmptyContent
import dev.shtanko.androidlab.github.presentation.shared.ErrorContent
import dev.shtanko.androidlab.github.presentation.shared.ItemLoading
import dev.shtanko.androidlab.github.presentation.shared.ItemNoMoreItems
import dev.shtanko.androidlab.github.presentation.shared.LoadingContent
import dev.shtanko.androidlab.github.presentation.shared.PullToRefresh
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import dev.shtanko.androidlab.utils.ThemesPreviews
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

@Composable
fun RepositoriesScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
    viewModel: RepositoriesViewModel = hiltViewModel(),
) {
    val repositoriesState = viewModel.repositoriesState.collectAsLazyPagingItems()
    val userState by viewModel.userState.collectAsStateWithLifecycle()
    val isRefreshing = viewModel.isRefreshing.collectAsStateWithLifecycle().value

    RepositoriesScreen(
        modifier = modifier,
        repositoriesState = repositoriesState,
        userState = userState,
        isRefreshing = repositoriesState.loadState.refresh is LoadState.Loading,
        navigateBack = navigateBack,
        onRefresh = remember { { viewModel.refresh() } },
        onTryAgainClick = remember { { viewModel.retry() } },
    )
}

@Composable
fun RepositoriesScreen(
    repositoriesState: LazyPagingItems<RepositoryResource>,
    userState: RepositoriesUiState,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = true,
    onTryAgainClick: () -> Unit = {},
    navigateBack: () -> Unit = {},
    onClick: (Int) -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    Surface {
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

                when (repositoriesState.loadState.refresh) {
                    is LoadState.Loading -> LoadingContent(
                        testTag = "RepositoriesLoadingContent",
                    )

                    is LoadState.Error -> ErrorContent(
                        onTryAgainClick = onTryAgainClick,
                    )

                    is LoadState.NotLoading -> RepositoriesList(
                        userState = userState,
                        pagingItems = repositoriesState,
                        isRefreshing = isRefreshing,
                        modifier = Modifier.padding(contentPadding),
                        onClick = onClick,
                        onRefresh = onRefresh,
                    )
                }
            }
        }
    }
}

@Composable
private fun RepositoriesList(
    pagingItems: LazyPagingItems<RepositoryResource>,
    userState: RepositoriesUiState,
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
            if (pagingItems.itemCount == 0) {
                EmptyContent(
                    content = stringResource(R.string.empty_repos_title),
                    testTag = "EmptyReposContent",
                )
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier,
                    content = {
                        item {
                            when (userState) {
                                RepositoriesUiState.Error -> Box(modifier = Modifier)
                                RepositoriesUiState.Loading -> Box(modifier = Modifier)
                                is RepositoriesUiState.Success -> UserDetailsHeaderItem(
                                    user = userState.user,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp),
                                )
                            }
                        }
                        item {
                            Text(
                                text = stringResource(id = R.string.repositories_title),
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    top = 16.dp,
                                    bottom = 8.dp,
                                ),
                                style = MaterialTheme.typography.headlineLarge,
                            )
                        }
                        items(
                            count = pagingItems.itemCount,
                            key = pagingItems.itemKey { it.id },
                            contentType = pagingItems.itemContentType { "Item" },
                        ) { index ->

                            val item = pagingItems[index]
                            item?.let {
                                RepositoryItem(
                                    item = it,
                                    onClick = onClick,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        }

                        pagingItems.apply {
                            when (loadState.append) {
                                is LoadState.Loading -> {
                                    item { ItemLoading() }
                                }

                                is LoadState.Error -> {
                                    item {
                                        ItemNoMoreItems()
                                    }
                                }

                                is LoadState.NotLoading -> item { Box(modifier = Modifier) }
                            }
                        }
                    },
                )
            }
        },
    )
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

@Composable
fun UserDetailsHeaderItem(
    user: UserFullResource,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier.padding(16.dp),
    ) {
        val maxImageSize = this.maxWidth / 2
        val imageSize = min(maxImageSize, 148.dp)
        Column {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth(),
            ) {
                UserDetailsImage(
                    modifier = Modifier
                        .size(imageSize)
                        .clip(MaterialTheme.shapes.large),
                    imageUrl = user.avatarUrl,
                    contentDescription = user.login,
                )
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .heightIn(imageSize),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {

                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = user.name.orEmpty(),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.headlineMedium,
                        )
                        user.company?.let {
                            Text(
                                text = it,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleLarge,
                            )

                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        user.company?.let {
                            UserDetailsItemIconInfo(
                                iconRes = R.drawable.ic_apartment,
                                text = it,
                            )
                        }
                        user.location?.let {
                            UserDetailsItemIconInfo(
                                iconRes = R.drawable.ic_location,
                                text = it,
                            )
                        }
                        UserDetailsItemIconInfo(
                            iconRes = R.drawable.ic_groups,
                            text = stringResource(R.string.followers_title, user.followers ?: 0),
                        )
                        UserDetailsItemIconInfo(
                            iconRes = R.drawable.ic_group,
                            text = stringResource(R.string.following_title, user.following ?: 0),
                        )
                    }
                }
            }
            user.bio?.let {
                UserDetailsDescription(
                    description = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                )
            }
        }
    }
}

@Composable
fun UserDetailsItemIconInfo(
    @DrawableRes
    iconRes: Int,
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 2.dp),
        )
    }
}

@ThemesPreviews
@Composable
private fun UserDetailsItemIconInfoPreview() {
    AndroidLabTheme {
        UserDetailsItemIconInfo(
            iconRes = R.drawable.ic_group,
            text = "150",
        )
    }
}

@ThemesPreviews
@Composable
private fun UserDetailsHeaderItemPreview() {
    AndroidLabTheme {
        UserDetailsHeaderItem(
            user = UserFullResource(
                id = 1,
                login = "login",
                avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                name = "Name",
                company = "Company",
                bio = LoremIpsum(100).values.first(),
            ),
        )
    }
}

@ThemesPreviews
@Composable
private fun RepositoriesTopAppBarPreview() {
    AndroidLabTheme {
        RepositoriesTopAppBar()
    }
}

@ThemesPreviews
@Composable
private fun RepositoriesScreenItemsPreview(
    @PreviewParameter(RepositoriesDataProvider::class) preview: Pair<UserFullResource, ImmutableList<RepositoryResource>>,
) {
    AndroidLabTheme {
        val pagingData = PagingData.from(preview.second)
        val flow = MutableStateFlow(pagingData)
        RepositoriesScreen(
            userState = RepositoriesUiState.Success(
                user = preview.first,
            ),
            repositoriesState = flow.collectAsLazyPagingItems(),
            isRefreshing = false,
        )
    }
}

@ThemesPreviews
@Composable
private fun RepositoriesScreenItemsLoadingPreview(
    @PreviewParameter(RepositoriesDataProvider::class) preview: Pair<UserFullResource, ImmutableList<RepositoryResource>>,
) {
    AndroidLabTheme {
        val pagingData = PagingData.from(preview.second)
        val flow = flowOf(pagingData)
        RepositoriesScreen(
            userState = RepositoriesUiState.Success(
                user = preview.first,
            ),
            repositoriesState = flow.collectAsLazyPagingItems(),
            isRefreshing = false,
        )
    }
}

@ThemesPreviews
@Composable
private fun RepositoriesScreenItemsEmptyPreview(
    @PreviewParameter(RepositoriesDataProvider::class) preview: Pair<UserFullResource, ImmutableList<RepositoryResource>>,
) {
    AndroidLabTheme {
        val pagingData = PagingData.empty<RepositoryResource>()
        val flow = MutableStateFlow(pagingData)
        RepositoriesScreen(
            userState = RepositoriesUiState.Success(
                user = preview.first,
            ),
            repositoriesState = flow.collectAsLazyPagingItems(),
            isRefreshing = false,
        )
    }
}

@ThemesPreviews
@Composable
private fun RepositoriesScreenItemsErrorPreview(
    @PreviewParameter(RepositoriesDataProvider::class) preview: Pair<UserFullResource, ImmutableList<RepositoryResource>>,
) {
    AndroidLabTheme {
        val pagingData = PagingData.from(
            data = emptyList<RepositoryResource>(),
            sourceLoadStates = LoadStates(
                LoadState.Error(Exception("Error")),
                LoadState.Error(Exception("Error")),
                LoadState.Error(Exception("Error")),
            ),
        )
        val flow = MutableStateFlow(pagingData)
        RepositoriesScreen(
            userState = RepositoriesUiState.Success(
                user = preview.first,
            ),
            repositoriesState = flow.collectAsLazyPagingItems(),
            isRefreshing = false,
        )
    }
}
