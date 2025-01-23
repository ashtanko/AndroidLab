package dev.shtanko.androidlab.github.data.model

import dev.shtanko.androidlab.github.data.db.entity.UserEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkUser(
    @SerialName("id") val id: Int,
    @SerialName("login") val login: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
)

fun NetworkUser.asEntity() = UserEntity(
    id = id,
    login = login,
    avatarUrl = avatarUrl,
)
