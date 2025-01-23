package dev.shtanko.androidlab.github.data.model

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class NetworkUserTest {
    private val jsonFormat = Json { ignoreUnknownKeys = true }

    @Test
    fun `serialize UserDto to JSON`() {
        val networkUser = NetworkUser(
            login = "testuser",
            id = 12_345,
            avatarUrl = "https://avatars.githubusercontent.com/u/12345?v=4",
        )

        val serialized = jsonFormat.encodeToString(NetworkUser.serializer(), networkUser)

        val expectedJson = """
            {
                "id": 12345,
                "login": "testuser",
                "avatar_url": "https://avatars.githubusercontent.com/u/12345?v=4"
            }
        """.trimIndent().replace("\n", "").replace(" ", "")

        assertEquals(expectedJson, serialized.replace("\n", "").replace(" ", ""))
    }

    @Test
    fun `deserialize JSON to UserDto`() {
        val jsonInput = """
            {
                "id": 12345,
                "login": "testuser",
                "avatar_url": "https://avatars.githubusercontent.com/u/12345?v=4"
            }
        """.trimIndent()

        val deserialized = jsonFormat.decodeFromString(NetworkUser.serializer(), jsonInput)

        val expected = NetworkUser(
            login = "testuser",
            id = 12_345,
            avatarUrl = "https://avatars.githubusercontent.com/u/12345?v=4",
        )

        assertEquals(expected, deserialized)
    }

    @Test
    fun `serialize and deserialize empty fields`() {
        val networkUser = NetworkUser(
            id = 12_345,
            login = "testuser",
        )

        val json = jsonFormat.encodeToString(NetworkUser.serializer(), networkUser)
        val deserialized = jsonFormat.decodeFromString(NetworkUser.serializer(), json)

        assertEquals(networkUser, deserialized)
        assertTrue(json.contains("testuser"))
    }
}
