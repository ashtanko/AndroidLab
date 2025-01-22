package dev.shtanko.androidlab.github.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.shtanko.androidlab.R
import dev.shtanko.androidlab.github.presentation.model.UserResource
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

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
fun UsersListContent(
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
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                state = state,
            )
        },
        modifier = modifier,
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                users.size,
                key = { index -> users[index].id },
            ) { index ->
                val item = users[index]
                UserItem(item, modifier = Modifier.clickable {
                    onUserClick.invoke(item.id)
                })
            }
        }
    }
}

@Composable
fun UsersLoadingContent(
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
            color = Color.Red,
            strokeWidth = 4.dp,
        )
    }
}

@Composable
fun UsersEmptyContent(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.padding(all = 32.dp),
            text = "No photos",
        )
    }
}

@Composable
fun UsersErrorContent(
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
fun UserItem(
    user: UserResource,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircularAvatarImage(
            sizeDp = 32.dp,
            imageUrl = user.avatarUrl,
            contentDescription = "Android",
        )
        Text(
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 8.dp),
            text = user.login.orEmpty(),
        )
    }
}

@Composable
fun CircularAvatarImage(
    imageUrl: String?,
    contentDescription: String,
    modifier: Modifier = Modifier,
    sizeDp: Dp = 64.dp,
    placeholderIcon: ImageVector = Icons.Default.Person,
    errorIcon: ImageVector = Icons.Default.Person,
) {
    Box(
        modifier = modifier
            .size(sizeDp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            placeholder = rememberVectorPainter(placeholderIcon),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            error = rememberVectorPainter(errorIcon),
            modifier = Modifier.fillMaxSize(),
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
            contentDescription = "Android",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UsersErrorContentPreview() {
    AndroidLabTheme {
        UsersErrorContent()
    }
}

@Preview(showBackground = true)
@Composable
private fun UsersLoadingContentPreview() {
    AndroidLabTheme {
        UsersLoadingContent()
    }
}

@Preview(showBackground = true)
@Composable
private fun UserItemPreview() {
    AndroidLabTheme {
        UserItem(
            user = UserResource(
                id = 1,
                login = LoremIpsum(50).values.first(),
                avatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
                type = "User",
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UsersListContentPreview() {
    AndroidLabTheme {
        UsersListContent(
            users = persistentListOf(
                UserResource(
                    id = 1,
                    login = LoremIpsum(10).values.first(),
                    avatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
                    type = "User",
                ),
                UserResource(
                    id = 2,
                    login = LoremIpsum(5).values.first(),
                    avatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
                    type = "User",
                ),
                UserResource(
                    id = 3,
                    login = LoremIpsum(50).values.first(),
                    avatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
                    type = "User",
                ),
            ),
        )
    }
}
