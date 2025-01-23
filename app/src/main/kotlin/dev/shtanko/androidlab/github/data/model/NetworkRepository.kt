package dev.shtanko.androidlab.github.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkRepository(
    @SerialName("id") val id: Int,
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
