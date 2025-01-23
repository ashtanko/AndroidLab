package dev.shtanko.androidlab.github.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.shtanko.androidlab.R
import dev.shtanko.androidlab.github.presentation.model.UserResource
import dev.shtanko.androidlab.github.presentation.preview.UsersPreviewDataProvider
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import kotlinx.collections.immutable.ImmutableList

@Composable
fun UsersScreen(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    UsersContent(
        uiState = uiState,
        modifier = modifier,
        isRefreshing = isRefreshing,
        onTryAgainClick = { viewModel.retry() },
        onUserClick = {},
        onRefresh = { viewModel.refresh() },
    )
}

@Composable
fun UsersContent(
    uiState: UserUiState,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = true,
    onTryAgainClick: () -> Unit = {},
    onUserClick: (Int) -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    when (uiState) {
        UserUiState.Error -> UsersErrorContent(modifier = modifier, onTryAgainClick)
        UserUiState.Loading -> UsersLoadingContent(modifier = modifier)
        is UserUiState.Success -> UsersListContent(
            users = uiState.users,
            modifier = modifier,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            onUserClick = onUserClick,
        )

        UserUiState.Empty -> UsersEmptyContent(modifier = modifier)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UsersListContent(
    users: ImmutableList<UserResource>,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = true,
    onUserClick: (Int) -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    val state = rememberPullToRefreshState()

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = 0,
    )

    PullToRefreshBox(
        state = state,
        isRefreshing = isRefreshing,
        onRefresh = {
            onRefresh()
        },
        indicator = {
            Indicator(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .testTag("PullToRefreshIndicator"),
                isRefreshing = isRefreshing,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                state = state,
            )
        },
        modifier = modifier.testTag("PullToRefreshBox"),
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            items(
                users.size,
                key = { index -> users[index].id + index },
            ) { index ->
                val item = users[index]
                UserItem(
                    item,
                    modifier = Modifier
                        .animateItem()
                        .clickable(
                            onClick = {
                                onUserClick.invoke(item.id)
                            },
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(
                                color = MaterialTheme.colorScheme.primaryContainer,
                            ),
                        ),
                )
            }
        }
    }
}

@Composable
private fun UsersLoadingContent(
    modifier: Modifier = Modifier,
    circularProgressSize: Dp = 32.dp,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(size = circularProgressSize)
                .testTag("loadingCircularProgress"),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeWidth = 4.dp,
        )
    }
}

@Composable
private fun UsersEmptyContent(
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
            text = stringResource(R.string.empty_users_title),
        )
    }
}

@Composable
private fun UsersErrorContent(
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

@Composable
private fun UserItem(
    user: UserResource,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircularAvatarImage(
            sizeDp = 32.dp,
            imageUrl = user.avatarUrl,
        )
        Text(
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 8.dp),
            text = user.login,
        )
    }
}

@Composable
private fun CircularAvatarImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    sizeDp: Dp = 64.dp,
    borderDp: Dp = 1.dp,
    placeholderIcon: ImageVector = Icons.Default.Person,
    errorIcon: ImageVector = Icons.Default.Person,
) {
    Box(
        modifier = modifier
            .size(sizeDp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .border(borderDp, MaterialTheme.colorScheme.primary, CircleShape),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            placeholder = rememberVectorPainter(placeholderIcon),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            error = rememberVectorPainter(errorIcon),
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UsersListContentPreview(
    @PreviewParameter(UsersPreviewDataProvider::class) preview: ImmutableList<UserResource>,
) {
    AndroidLabTheme {
        UsersContent(
            uiState = UserUiState.Success(
                users = preview,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UsersErrorContentPreview() {
    AndroidLabTheme {
        UsersContent(
            uiState = UserUiState.Error,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UsersLoadingContentPreview() {
    AndroidLabTheme {
        UsersContent(
            uiState = UserUiState.Loading,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UsersEmptyContentPreview() {
    AndroidLabTheme {
        UsersContent(
            uiState = UserUiState.Empty,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserItemPreview(
    @PreviewParameter(UsersPreviewDataProvider::class) preview: ImmutableList<UserResource>,
) {
    AndroidLabTheme {
        UserItem(
            user = preview.first(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CircularAvatarImagePreview() {
    AndroidLabTheme {
        CircularAvatarImage(
            sizeDp = 32.dp,
            imageUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
        )
    }
}
