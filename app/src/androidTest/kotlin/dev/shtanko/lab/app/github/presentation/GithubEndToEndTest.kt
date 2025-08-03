package dev.shtanko.lab.app.github.presentation

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dev.shtanko.androidlab.core.designsystem.theme.AndroidLabTheme
import dev.shtanko.lab.app.MainActivity
import dev.shtanko.lab.app.github.GithubApp
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
