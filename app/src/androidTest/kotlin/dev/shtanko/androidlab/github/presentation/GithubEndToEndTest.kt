package dev.shtanko.androidlab.github.presentation

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dev.shtanko.androidlab.MainActivity
import dev.shtanko.androidlab.github.GithubApp
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import org.junit.Before
import org.junit.Rule

class GithubEndToEndTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        composeRule.activity.setContent {
            AndroidLabTheme {
                GithubApp(
                    displayFeatures = emptyList(),
                )
            }
        }
    }
}
