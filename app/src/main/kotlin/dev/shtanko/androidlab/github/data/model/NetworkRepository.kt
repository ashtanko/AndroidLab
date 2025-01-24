package dev.shtanko.androidlab.github.data.model

import dev.shtanko.androidlab.github.data.db.entity.RepositoryEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkRepository(
    @SerialName("id") val id: Int,
    @SerialName("node_id") val repositoryId: String,
    @SerialName("name") val name: String? = null,
    @SerialName("full_name") val fullName: String? = null,
    @SerialName("private") val isPrivate: Boolean? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("fork") val isFork: Boolean? = null,
    @SerialName("size") val size: Int? = null,
    @SerialName("stargazers_count") val stars: Int? = null,
    @SerialName("watchers_count") val watchers: Int? = null,
    @SerialName("forks_count") val forks: Int? = null,
    @SerialName("language") val language: String? = null,
    @SerialName("has_issues") val hasIssues: Boolean? = null,
    @SerialName("has_projects") val hasProjects: Boolean? = null,
    @SerialName("archived") val archived: Boolean? = null,
    @SerialName("disabled") val disabled: Boolean? = null,
    @SerialName("open_issues_count") val openIssues: Int? = null,
    @SerialName("is_template") val isTemplate: Int? = null,
)

fun NetworkRepository.asEntity() = RepositoryEntity(
    id = id,
    repositoryId = repositoryId,
    name = name,
    fullName = fullName,
    isPrivate = isPrivate,
    description = description,
    isFork = isFork,
    size = size,
    stars = stars,
    watchers = watchers,
    forks = forks,
    language = language,
    hasIssues = hasIssues,
    hasProjects = hasProjects,
    archived = archived,
    disabled = disabled,
    openIssues = openIssues,
    isTemplate = isTemplate,
)
