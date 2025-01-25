package dev.shtanko.androidlab.github.presentation.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.shtanko.androidlab.github.presentation.model.RepositoryResource
import dev.shtanko.androidlab.github.presentation.model.UserResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

class RepositoriesDataProvider : PreviewParameterProvider<ImmutableList<RepositoryResource>> {
    override val values: Sequence<ImmutableList<RepositoryResource>>
        get() = sequenceOf(
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
