package dev.shtanko.androidlab.github.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class UserResource(
    val id: Int = 0,
    val login: String = "",
    val avatarUrl: String? = "",
)
