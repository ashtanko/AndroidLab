package dev.shtanko.androidlab.github.presentation.users

import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import dev.shtanko.androidlab.MainActivity
import dev.shtanko.androidlab.R
import dev.shtanko.androidlab.github.presentation.model.UserResource
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.junit.Rule
import org.junit.Test

class UserScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun whenLoadingState_thenLoadingContentIsDisplayed() {
        composeTestRule.activity.setContent {
            UsersContent(uiState = UserUiState.Loading)
        }
        composeTestRule.onNode(hasTestTag("loadingCircularProgress"))
            .assertIsDisplayed()
    }

    @Test
    fun whenErrorState_thenErrorContentIsDisplayed() {
        val context = composeTestRule.activity.applicationContext
        composeTestRule.activity.setContent {
            UsersContent(uiState = UserUiState.Error)
        }
        composeTestRule.onNode(hasText(context.resources.getString(R.string.try_again)))
        composeTestRule.onNode(hasTestTag("tryAgainButton"))
            .assertIsDisplayed()
    }

    @Test
    fun whenEmptyState_thenEmptyContentIsDisplayed() {
        val context = composeTestRule.activity.applicationContext
        composeTestRule.activity.setContent {
            UsersContent(
                uiState = UserUiState.Empty,
            )
        }
        composeTestRule.onNode(hasText(context.resources.getString(R.string.empty_users_title)))
            .assertIsDisplayed()
        composeTestRule.onNode(hasTestTag("EmptyUsersContent"))
            .assertIsDisplayed()
    }

    @Test
    fun whenTryAgainButtonClicked_thenCallbackIsInvoked() {
        var wasClicked = false

        composeTestRule.activity.setContent {
            UsersContent(
                uiState = UserUiState.Error,
                onTryAgainClick = { wasClicked = true },
            )
        }
        composeTestRule.onNode(hasTestTag("tryAgainButton")).performClick()
        assert(wasClicked)
    }

    @Test
    fun whenUsersLoaded_thenDisplaysCorrectNumberOfItems() {
        composeTestRule.activity.setContent {
            UsersContent(
                uiState = UserUiState.Success(mockUsers),
                isRefreshing = false,
            )
        }
        mockUsers.forEachIndexed { index, user ->
            composeTestRule.onNodeWithText(user.login).assertIsDisplayed()
        }
    }

    @Test
    fun whenRefreshing_thenRefreshIndicatorIsVisible() {
        val isRefreshing = mutableStateOf(true)

        composeTestRule.activity.setContent {
            UsersContent(
                uiState = UserUiState.Success(emptyList<UserResource>().toImmutableList()),
                isRefreshing = isRefreshing.value,
                onRefresh = { /* Do nothing */ },
            )
        }

        // Assert that the refresh indicator is displayed
        composeTestRule.onNodeWithTag("PullToRefreshIndicator").assertIsDisplayed()

        // Simulate end of refreshing
        isRefreshing.value = false
        composeTestRule.waitForIdle()

        // Assert that the refresh indicator is no longer displayed
        // composeTestRule.onNodeWithTag("PullToRefreshIndicator").assertDoesNotExist() // todo fix
    }

    @Test
    fun whenUserClicked_thenOnUserClickCallbackIsInvoked() {
        val clickedUserId = mutableStateOf<Int?>(null)
        composeTestRule.activity.setContent {
            UsersContent(
                uiState = UserUiState.Success(mockUsers),
                isRefreshing = false,
                onUserClick = { clickedUserId.value = it },
            )
        }

        // Click on the second user
        composeTestRule.onNodeWithText("User 2").performClick()

        // Assert that the correct user ID was passed
        assert(clickedUserId.value == 2)
    }

    @Test
    fun whenPullToRefresh_thenOnRefreshCallbackIsInvoked() {
        var refreshCalled = false

        composeTestRule.activity.setContent {
            UsersContent(
                uiState = UserUiState.Success(emptyList<UserResource>().toImmutableList()),
                isRefreshing = false,
                onRefresh = { refreshCalled = true },
            )
        }

        // Simulate a pull-to-refresh gesture
        composeTestRule.onNodeWithTag("PullToRefreshBox").performTouchInput {
            swipeDown()
        }
        composeTestRule.waitForIdle()

        // Assert that the refresh callback was triggered
        assert(refreshCalled)
    }

    private val mockUsers = persistentListOf(
        UserResource(id = 1, login = "User 1"),
        UserResource(id = 2, login = "User 2"),
        UserResource(id = 3, login = "User 3"),
    )
}
