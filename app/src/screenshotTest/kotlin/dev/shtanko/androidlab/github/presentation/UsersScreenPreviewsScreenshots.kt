package dev.shtanko.androidlab.github.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.shtanko.androidlab.github.presentation.model.UserResource
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme

class UsersScreenPreviewsScreenshots {
    @Preview(showBackground = true)
    @Composable
    fun UsersErrorContentPreview() {
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
                    login = "Android",
                    avatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
                    type = "User",
                ),
            )
        }
    }
}
