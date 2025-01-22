package dev.shtanko.androidlab.github.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class UserScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingUsers_loadingContentPresent() {
        composeTestRule.setContent {
            UsersContent(uiState = UserUiState.Loading)
        }
        composeTestRule.onNode(hasTestTag("loadingCircularProgress"))
            .assertIsDisplayed()
    }

    @Test
    fun errorUsers_errorContentPresent() {
        composeTestRule.setContent {
            UsersContent(uiState = UserUiState.Error)
        }
        composeTestRule.onNode(hasTestTag("tryAgainButton"))
            .assertIsDisplayed()
    }

    @Test
    fun testUsersErrorContentButtonClickInvokesCallback() {
        var wasClicked = false

        composeTestRule.setContent {
            UsersErrorContent(onTryAgainClick = { wasClicked = true })
        }

        // Perform a click on the button
        composeTestRule.onNode(hasTestTag("tryAgainButton")).performClick()

        // Assert that the click callback was invoked
        assert(wasClicked)
    }
}
