package dev.shtanko.lab.app.github.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class RepositoryResource(
    val id: Int,
    val repositoryId: String,
    val name: String? = null,
    val fullName: String? = null,
    val isPrivate: Boolean? = null,
    val description: String? = null,
    val isFork: Boolean? = null,
    val size: Int? = null,
    val stars: Int? = null,
    val watchers: Int? = null,
    val forks: Int? = null,
    val language: String? = null,
    val hasIssues: Boolean? = null,
    val hasProjects: Boolean? = null,
    val archived: Boolean? = null,
    val disabled: Boolean? = null,
    val openIssues: Int? = null,
    val isTemplate: Boolean? = false,
    val owner: UserResource? = null,
)
