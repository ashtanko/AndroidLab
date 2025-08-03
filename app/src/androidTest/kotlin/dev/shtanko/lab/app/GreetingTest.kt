package dev.shtanko.lab.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GreetingTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun greeting_currentLabelExists() {
        composeTestRule.setContent {
            Greeting(name = "Android")
        }
        composeTestRule.onNodeWithText("Hello Android!").assertIsDisplayed()
    }
}
