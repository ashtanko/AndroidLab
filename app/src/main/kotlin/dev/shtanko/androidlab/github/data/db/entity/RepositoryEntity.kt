package dev.shtanko.androidlab.github.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.shtanko.androidlab.github.presentation.model.RepositoryResource

@Entity(tableName = "repos")
data class RepositoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo("repository_id") val repositoryId: String,
    @ColumnInfo("name") val name: String? = null,
    @ColumnInfo("full_name") val fullName: String? = null,
    @ColumnInfo("private") val isPrivate: Boolean? = null,
    @ColumnInfo("description") val description: String? = null,
    @ColumnInfo("fork") val isFork: Boolean? = null,
    @ColumnInfo("size") val size: Int? = null,
    @ColumnInfo("stargazers_count") val stars: Int? = null,
    @ColumnInfo("watchers_count") val watchers: Int? = null,
    @ColumnInfo("forks_count") val forks: Int? = null,
    @ColumnInfo("language") val language: String? = null,
    @ColumnInfo("has_issues") val hasIssues: Boolean? = null,
    @ColumnInfo("has_projects") val hasProjects: Boolean? = null,
    @ColumnInfo("archived") val archived: Boolean? = null,
    @ColumnInfo("disabled") val disabled: Boolean? = null,
    @ColumnInfo("open_issues_count") val openIssues: Int? = null,
    @ColumnInfo("is_template") val isTemplate: Int? = null,
    @ColumnInfo(name = "page")
    val page: Int = 0,
)

@Entity(tableName = "repos_remote_key")
data class RepositoryRemoteKeysEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "repository_id")
    val repositoryId: String,
    val prevKey: Int?,
    val currentPage: Int,
    val nextKey: Int?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
)

fun RepositoryEntity.asExternalModel() = RepositoryResource(
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
