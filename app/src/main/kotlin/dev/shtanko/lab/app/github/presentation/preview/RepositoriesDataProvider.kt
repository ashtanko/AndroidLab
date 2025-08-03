package dev.shtanko.lab.app.github.presentation.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.shtanko.lab.app.github.presentation.model.RepositoryResource
import dev.shtanko.lab.app.github.presentation.model.UserFullResource
import dev.shtanko.lab.app.github.presentation.model.UserResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

class RepositoriesDataProvider :
    PreviewParameterProvider<Pair<UserFullResource, ImmutableList<RepositoryResource>>> {
    override val values: Sequence<Pair<UserFullResource, ImmutableList<RepositoryResource>>>
        get() = sequenceOf(
            UserFullResource(
                id = 1,
                login = "ashtanko",
                avatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
                name = "Oleksii Shtanko",
                company = "JetBrains",
                blog = "https://shtanko.dev",
                location = "Lisbon, Portugal",
            )
                to
                persistentListOf(
                    RepositoryResource(
                        id = 1,
                        repositoryId = "1",
                        name = "AndroidLab",
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
                        name = "AndroidLab",
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
                        name = "AndroidLab",
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
                ),
        )
}
