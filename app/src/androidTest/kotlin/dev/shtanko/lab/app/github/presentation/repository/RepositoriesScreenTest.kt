package dev.shtanko.lab.app.github.presentation.repository

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import dev.shtanko.androidlab.core.designsystem.theme.AndroidLabTheme
import dev.shtanko.lab.app.MainActivity
import dev.shtanko.lab.app.R
import dev.shtanko.lab.app.github.presentation.model.RepositoryResource
import dev.shtanko.lab.app.github.presentation.model.UserFullResource
import dev.shtanko.lab.app.github.presentation.model.UserResource
import dev.shtanko.lab.app.github.presentation.repositories.RepositoriesScreen
import dev.shtanko.lab.app.github.presentation.repositories.RepositoriesUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class RepositoriesScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun whenEmptyState_thenEmptyContentIsDisplayed() {
        val context = composeTestRule.activity.applicationContext
        composeTestRule.activity.setContent {
            AndroidLabTheme {
                val pagingData = PagingData.empty<RepositoryResource>()
                val flow = MutableStateFlow(pagingData)
                RepositoriesScreen(
                    uiState = RepositoriesUiState.Success(
                        user = mockUser,
                        repositories = flow,
                    ),
                    isRefreshing = false,
                )
            }
        }
        composeTestRule.onNode(hasText(context.resources.getString(R.string.empty_repos_title)))
            .assertIsDisplayed()
        composeTestRule.onNode(hasTestTag("EmptyReposContent"))
            .assertIsDisplayed()
    }

    @Test
    fun whenLoadingState_thenLoadingContentIsDisplayed() {
        composeTestRule.activity.setContent {
            AndroidLabTheme {
                RepositoriesScreen(
                    uiState = RepositoriesUiState.Loading,
                    isRefreshing = false,
                )
            }
        }
        composeTestRule.onNode(hasTestTag("RepositoriesLoadingContent"))
            .assertIsDisplayed()
    }

    @Test
    fun whenErrorState_thenErrorContentIsDisplayed() {
        val context = composeTestRule.activity.applicationContext
        composeTestRule.activity.setContent {
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
                    uiState = RepositoriesUiState.Error,
                    isRefreshing = false,
                )
            }
        }
        composeTestRule.onNode(hasText(context.resources.getString(R.string.try_again)))
        composeTestRule.onNode(hasTestTag("tryAgainButton"))
            .assertIsDisplayed()
    }

    @Test
    fun whenReposLoaded_thenDisplaysCorrectNumberOfItems() {
        composeTestRule.activity.setContent {
            AndroidLabTheme {
                val pagingData = PagingData.from<RepositoryResource>(mockRepos)
                val flow = MutableStateFlow(pagingData)
                RepositoriesScreen(
                    uiState = RepositoriesUiState.Success(
                        user = mockUser,
                        repositories = flow,
                    ),
                    isRefreshing = false,
                )
            }
        }
        mockRepos.forEach { repo ->
            composeTestRule.onNodeWithText(repo.name.orEmpty()).assertIsDisplayed()
        }
    }

    @Test
    fun whenUserLoaded_thenDataDisplaysCorrect() {
        composeTestRule.activity.setContent {
            AndroidLabTheme {
                val pagingData = PagingData.from<RepositoryResource>(mockRepos)
                val flow = MutableStateFlow(pagingData)
                RepositoriesScreen(
                    uiState = RepositoriesUiState.Success(
                        user = mockUser,
                        repositories = flow,
                    ),
                    isRefreshing = false,
                )
            }
        }
        composeTestRule.onNodeWithText(mockUser.name.orEmpty()).assertIsDisplayed()
        composeTestRule.onNodeWithText(mockUser.location.orEmpty()).assertIsDisplayed()
    }

    private val mockUser = UserFullResource(
        id = 1,
        login = "ashtanko",
        avatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
        name = "Oleksii Shtanko",
        company = "JetBrains",
        blog = "https://shtanko.dev",
        location = "Lisbon, Portugal",
    )

    private val mockRepos = persistentListOf(
        RepositoryResource(
            id = 1,
            repositoryId = "1",
            name = "AndroidLab1",
            fullName = "dev.shtanko/androidlab",
            isPrivate = false,
            description = "AndroidLab is a project that demonstrates the usage of modern Android development tools and libraries.",
            isFork = false,
            size = 100,
            stars = 100,
            watchers = 100,
            forks = 100,
            language = "Kotlin",
            hasIssues = true,
            hasProjects = true,
            archived = false,
            disabled = false,
            openIssues = 10,
            isTemplate = false,
            owner = UserResource(
                id = 1,
                login = "shtanko",
                avatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
            ),
        ),
        RepositoryResource(
            id = 2,
            repositoryId = "2",
            name = "AndroidLab2",
            fullName = "dev.shtanko/androidlab",
            isPrivate = false,
            description = "AndroidLab is a project that demonstrates the usage of modern Android development tools and libraries.",
            isFork = false,
            size = 100,
            stars = 100,
            watchers = 100,
            forks = 100,
            language = "Kotlin",
            hasIssues = true,
            hasProjects = true,
            archived = false,
            disabled = false,
            openIssues = 10,
            isTemplate = false,
            owner = UserResource(
                id = 2,
                login = "shtanko",
                avatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
            ),
        ),
        RepositoryResource(
            id = 3,
            repositoryId = "3",
            name = "AndroidLab3",
            fullName = "dev.shtanko/androidlab",
            isPrivate = false,
            description = "AndroidLab is a project that demonstrates the usage of modern Android development tools and libraries.",
            isFork = false,
            size = 100,
            stars = 100,
            watchers = 100,
            forks = 100,
            language = "Kotlin",
            hasIssues = true,
            hasProjects = true,
            archived = false,
            disabled = false,
            openIssues = 10,
            isTemplate = false,
            owner = UserResource(
                id = 3,
                login = "shtanko",
                avatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
            ),
        ),
    )
}
