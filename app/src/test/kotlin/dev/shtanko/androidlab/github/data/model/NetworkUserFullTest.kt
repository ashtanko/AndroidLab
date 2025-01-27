package dev.shtanko.androidlab.github.data.model

import dev.shtanko.androidlab.utils.toDate
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class NetworkUserFullTest {
    private val jsonFormat = Json { ignoreUnknownKeys = true }

    @Test
    fun `serialize NetworkUser to JSON`() {

        val networkUser = NetworkUserFull(
            login = "ashtanko",
            id = 10_870_984,
            avatarUrl = "https://avatars.githubusercontent.com/u/10870984?v=4",
            name = "Oleksii Shtanko",
            company = "@EPAM",
            blog = "https://shtanko.dev",
            location = "Lisbon, Portugal",
            hireable = true,
            bio = "Senior Android Engineer. \r\n\r\nI am passionate about Android, Flutter and Golang",
            twitter = "shtankopro",
            publicReposCount = 29,
            publicGistsCount = 8,
            followers = 98,
            following = 154,
            createdAt = "2015-02-05T19:17:05Z".toDate(),
        )

        val serialized = jsonFormat.encodeToString(NetworkUserFull.serializer(), networkUser)

        val expectedJson = """
            {
                "id": 10870984,
                "login": "ashtanko",
                "avatar_url": "https://avatars.githubusercontent.com/u/10870984?v=4",
                "name": "Oleksii Shtanko",
                "company": "@EPAM",
                "blog": "https://shtanko.dev",
                "location": "Lisbon, Portugal",
                "hireable": true,
                "bio": "Senior Android Engineer. \r\n\r\nI am passionate about Android, Flutter and Golang",
                "twitter_username": "shtankopro",
                "public_repos": 29,
                "public_gists": 8,
                "followers": 98,
                "following": 154,
                "created_at": "2015-02-05T19:17:05Z"
            }
    """.trimIndent()

        assertEquals(
            expectedJson.replace("\n", "").replace(" ", ""),
            serialized.replace("\n", "").replace(" ", ""),
        )
    }

    @Test
    fun `deserialize JSON to NetworkUserFull`() {
        val jsonInput = """
            {
                "id": 10870984,
                "login": "ashtanko",
                "avatar_url": "https://avatars.githubusercontent.com/u/10870984?v=4",
                "name": "Oleksii Shtanko",
                "company": "@EPAM",
                "blog": "https://shtanko.dev",
                "location": "Lisbon, Portugal",
                "hireable": true,
                "bio": "Senior Android Engineer. \r\n\r\nI am passionate about Android, Flutter and Golang",
                "twitter_username": "shtankopro",
                "public_repos": 29,
                "public_gists": 8,
                "followers": 98,
                "following": 154,
                "created_at": "2015-02-05T19:17:05Z"
            }
        """.trimIndent()
        val deserialized = jsonFormat.decodeFromString(NetworkUserFull.serializer(), jsonInput)

        val expected = NetworkUserFull(
            login = "ashtanko",
            id = 10_870_984,
            avatarUrl = "https://avatars.githubusercontent.com/u/10870984?v=4",
            name = "Oleksii Shtanko",
            company = "@EPAM",
            blog = "https://shtanko.dev",
            location = "Lisbon, Portugal",
            hireable = true,
            bio = "Senior Android Engineer. \r\n\r\nI am passionate about Android, Flutter and Golang",
            twitter = "shtankopro",
            publicReposCount = 29,
            publicGistsCount = 8,
            followers = 98,
            following = 154,
            createdAt = "2015-02-05T19:17:05Z".toDate(),
        )

        assertEquals(expected, deserialized)
    }

    @Test
    fun `serialize and deserialize empty fields`() {
        val networkUser = NetworkUserFull(
            id = 12_345,
            login = "testuser",
        )

        val json = jsonFormat.encodeToString(NetworkUserFull.serializer(), networkUser)
        val deserialized = jsonFormat.decodeFromString(NetworkUserFull.serializer(), json)

        assertEquals(networkUser, deserialized)
        assertTrue(json.contains("testuser"))
    }

    @Test
    fun `NetworkUserFull to UserEntityFull mapping`() {
        val networkUser = NetworkUserFull(
            login = "ashtanko",
            id = 10_870_984,
            avatarUrl = "https://avatars.githubusercontent.com/u/10870984?v=4",
            name = "Oleksii Shtanko",
            company = "@EPAM",
            blog = "https://shtanko.dev",
            location = "Lisbon, Portugal",
            hireable = true,
            bio = "Senior Android Engineer. \r\n\r\nI am passionate about Android, Flutter and Golang",
            twitter = "shtankopro",
            publicReposCount = 29,
            publicGistsCount = 8,
            followers = 98,
            following = 154,
            createdAt = "2015-02-05T19:17:05Z".toDate(),
        )

        val userEntity = networkUser.asEntity()

        assertEquals(networkUser.id, userEntity.id)
        assertEquals(networkUser.login, userEntity.login)
        assertEquals(networkUser.avatarUrl, userEntity.avatarUrl)
        assertEquals(networkUser.name, userEntity.name)
        assertEquals(networkUser.company, userEntity.company)
        assertEquals(networkUser.blog, userEntity.blog)
        assertEquals(networkUser.location, userEntity.location)
        assertEquals(networkUser.hireable, userEntity.hireable)
        assertEquals(networkUser.bio, userEntity.bio)
        assertEquals(networkUser.twitter, userEntity.twitter)
        assertEquals(networkUser.publicReposCount, userEntity.publicReposCount)
        assertEquals(networkUser.publicGistsCount, userEntity.publicGistsCount)
        assertEquals(networkUser.followers, userEntity.followers)
        assertEquals(networkUser.following, userEntity.following)
        assertEquals(networkUser.createdAt, userEntity.createdAt)
    }
}
