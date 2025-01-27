package dev.shtanko.androidlab.github.presentation.model

import androidx.compose.runtime.Immutable
import java.util.Date

@Immutable
data class UserResource(
    val id: Int = 0,
    val login: String = "",
    val avatarUrl: String? = "",
)

@Immutable
data class UserFullResource(
    val id: Int = 0,
    val login: String? = null,
    val avatarUrl: String? = null,
    val name: String? = null,
    val company: String? = null,
    val blog: String? = null,
    val location: String? = null,
    val hireable: Boolean? = null,
    val email: String? = null,
    val bio: String? = null,
    val twitter: String? = null,
    val publicReposCount: Int? = 0,
    val publicGistsCount: Int? = 0,
    val followers: Int? = 0,
    val following: Int? = 0,
    val createdAt: Date? = null,
)
