package dev.shtanko.lab.app.github.data.model

import dev.shtanko.lab.app.github.data.db.entity.UserEntityFull
import dev.shtanko.lab.app.utils.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class NetworkUserFull(
    @SerialName("id") val id: Int,
    @SerialName("login") val login: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("company") val company: String? = null,
    @SerialName("blog") val blog: String? = null,
    @SerialName("location") val location: String? = null,
    @SerialName("hireable") val hireable: Boolean? = null,
    @SerialName("bio") val bio: String? = null,
    @SerialName("twitter_username") val twitter: String? = null,
    @SerialName("public_repos") val publicReposCount: Int? = null,
    @SerialName("public_gists") val publicGistsCount: Int? = null,
    @SerialName("followers") val followers: Int? = null,
    @SerialName("following") val following: Int? = null,
    @Serializable(with = DateSerializer::class)
    @SerialName("created_at") val createdAt: Date? = null,
)

fun NetworkUserFull.asEntity() = UserEntityFull(
    id = id,
    login = login,
    avatarUrl = avatarUrl,
    name = name,
    company = company,
    blog = blog,
    location = location,
    hireable = hireable,
    bio = bio,
    twitter = twitter,
    publicReposCount = publicReposCount,
    publicGistsCount = publicGistsCount,
    followers = followers,
    following = following,
    createdAt = createdAt,
)
