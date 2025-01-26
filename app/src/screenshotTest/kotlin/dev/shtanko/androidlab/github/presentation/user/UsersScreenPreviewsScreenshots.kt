package dev.shtanko.androidlab.github.presentation.user

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import dev.shtanko.androidlab.github.presentation.model.UserResource
import dev.shtanko.androidlab.github.presentation.users.UserUiState
import dev.shtanko.androidlab.github.presentation.users.UsersContent
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import kotlinx.collections.immutable.persistentListOf

class UsersScreenPreviewsScreenshots {
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
    fun UsersErrorContentPreview() {
        AndroidLabTheme {
            UsersContent(
                uiState = UserUiState.Error,
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
    private fun UsersLoadingContentPreview() {
        AndroidLabTheme {
            UsersContent(
                uiState = UserUiState.Loading,
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
    private fun UserItemPreview() {
        AndroidLabTheme {
            UsersContent(
                uiState = UserUiState.Success(
                    persistentListOf(
                        UserResource(
                            id = 1,
                            login = LoremIpsum(10).values.first(),
                            avatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
                        ),
                    ),
                ),
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
    private fun UsersEmptyContentPreview() {
        AndroidLabTheme {
            UsersContent(
                uiState = UserUiState.Empty,
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
    private fun UsersListContentPreview() {
        AndroidLabTheme {
            UsersContent(
                uiState = UserUiState.Success(
                    users = persistentListOf(
                        UserResource(
                            id = 1,
                            login = LoremIpsum(10).values.first(),
                            avatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
                        ),
                        UserResource(
                            id = 2,
                            login = LoremIpsum(5).values.first(),
                            avatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
                        ),
                        UserResource(
                            id = 3,
                            login = LoremIpsum(50).values.first(),
                            avatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
                        ),
                    ),
                ),
            )
        }
    }
}
