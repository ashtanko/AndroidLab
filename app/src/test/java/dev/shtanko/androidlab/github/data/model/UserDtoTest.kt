package dev.shtanko.androidlab.github.data.model

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class UserDtoTest {
    private val jsonFormat = Json { ignoreUnknownKeys = true }

    @Test
    fun `serialize UserDto to JSON`() {
        val userDto = UserDto(
            login = "testuser",
            id = 12_345,
            nodeId = "MDQ6VXNlcjEyMzQ1",
            avatarUrl = "https://avatars.githubusercontent.com/u/12345?v=4",
            gravatarId = "",
            url = "https://api.github.com/users/testuser",
            htmlUrl = "https://github.com/testuser",
            followersUrl = "https://api.github.com/users/testuser/followers",
            followingUrl = "https://api.github.com/users/testuser/following{/other_user}",
            gistsUrl = "https://api.github.com/users/testuser/gists{/gist_id}",
            starredUrl = "https://api.github.com/users/testuser/starred{/owner}{/repo}",
            subscriptionsUrl = "https://api.github.com/users/testuser/subscriptions",
            organizationsUrl = "https://api.github.com/users/testuser/orgs",
            reposUrl = "https://api.github.com/users/testuser/repos",
            eventsUrl = "https://api.github.com/users/testuser/events{/privacy}",
            receivedEventsUrl = "https://api.github.com/users/testuser/received_events",
            type = "User",
            userViewType = "Public",
            siteAdmin = false,
        )

        val serialized = jsonFormat.encodeToString(UserDto.serializer(), userDto)

        val expectedJson = """
            {
                "id": 12345,
                "login": "testuser",
                "node_id": "MDQ6VXNlcjEyMzQ1",
                "avatar_url": "https://avatars.githubusercontent.com/u/12345?v=4",
                "gravatar_id": "",
                "url": "https://api.github.com/users/testuser",
                "html_url": "https://github.com/testuser",
                "followers_url": "https://api.github.com/users/testuser/followers",
                "following_url": "https://api.github.com/users/testuser/following{/other_user}",
                "gists_url": "https://api.github.com/users/testuser/gists{/gist_id}",
                "starred_url": "https://api.github.com/users/testuser/starred{/owner}{/repo}",
                "subscriptions_url": "https://api.github.com/users/testuser/subscriptions",
                "organizations_url": "https://api.github.com/users/testuser/orgs",
                "repos_url": "https://api.github.com/users/testuser/repos",
                "events_url": "https://api.github.com/users/testuser/events{/privacy}",
                "received_events_url": "https://api.github.com/users/testuser/received_events",
                "type": "User",
                "user_view_type": "Public",
                "site_admin": false
            }
        """.trimIndent().replace("\n", "").replace(" ", "")

        assertEquals(expectedJson, serialized.replace("\n", "").replace(" ", ""))
    }

    @Test
    fun `deserialize JSON to UserDto`() {
        val jsonInput = """
            {
                "login": "testuser",
                "id": 12345,
                "node_id": "MDQ6VXNlcjEyMzQ1",
                "avatar_url": "https://avatars.githubusercontent.com/u/12345?v=4",
                "gravatar_id": "",
                "url": "https://api.github.com/users/testuser",
                "html_url": "https://github.com/testuser",
                "followers_url": "https://api.github.com/users/testuser/followers",
                "following_url": "https://api.github.com/users/testuser/following{/other_user}",
                "gists_url": "https://api.github.com/users/testuser/gists{/gist_id}",
                "starred_url": "https://api.github.com/users/testuser/starred{/owner}{/repo}",
                "subscriptions_url": "https://api.github.com/users/testuser/subscriptions",
                "organizations_url": "https://api.github.com/users/testuser/orgs",
                "repos_url": "https://api.github.com/users/testuser/repos",
                "events_url": "https://api.github.com/users/testuser/events{/privacy}",
                "received_events_url": "https://api.github.com/users/testuser/received_events",
                "type": "User",
                "user_view_type": "Public",
                "site_admin": false
            }
        """.trimIndent()

        val deserialized = jsonFormat.decodeFromString(UserDto.serializer(), jsonInput)

        val expected = UserDto(
            login = "testuser",
            id = 12_345,
            nodeId = "MDQ6VXNlcjEyMzQ1",
            avatarUrl = "https://avatars.githubusercontent.com/u/12345?v=4",
            gravatarId = "",
            url = "https://api.github.com/users/testuser",
            htmlUrl = "https://github.com/testuser",
            followersUrl = "https://api.github.com/users/testuser/followers",
            followingUrl = "https://api.github.com/users/testuser/following{/other_user}",
            gistsUrl = "https://api.github.com/users/testuser/gists{/gist_id}",
            starredUrl = "https://api.github.com/users/testuser/starred{/owner}{/repo}",
            subscriptionsUrl = "https://api.github.com/users/testuser/subscriptions",
            organizationsUrl = "https://api.github.com/users/testuser/orgs",
            reposUrl = "https://api.github.com/users/testuser/repos",
            eventsUrl = "https://api.github.com/users/testuser/events{/privacy}",
            receivedEventsUrl = "https://api.github.com/users/testuser/received_events",
            type = "User",
            userViewType = "Public",
            siteAdmin = false,
        )

        assertEquals(expected, deserialized)
    }

    @Test
    fun `serialize and deserialize empty fields`() {
        val userDto = UserDto(
            id = 12_345,
            login = "testuser",
            nodeId = null,
        )

        val json = jsonFormat.encodeToString(UserDto.serializer(), userDto)
        val deserialized = jsonFormat.decodeFromString(UserDto.serializer(), json)

        assertEquals(userDto, deserialized)
        assertTrue(json.contains("testuser"))
    }
}
