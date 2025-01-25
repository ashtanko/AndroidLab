package dev.shtanko.androidlab.github.data.model

import dev.shtanko.androidlab.github.data.db.entity.RepositoryEntity
import dev.shtanko.androidlab.github.data.db.entity.UserEntity
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class NetworkRepositoryTest {
    private val jsonFormat = Json { ignoreUnknownKeys = true }

    @Test
    fun `serialize NetworkRepository to JSON`() {
        val networkRepository = NetworkRepository(
            id = 1,
            repositoryId = "MDEwOlJlcG9zaXRvcnkzMjAwNDEwNzE=",
            name = "Sample Repo",
            fullName = "sample/sample-repo",
            isPrivate = false,
            description = "A sample repository",
            isFork = false,
            size = 1024,
            stars = 150,
            watchers = 75,
            forks = 10,
            language = "Kotlin",
            hasIssues = true,
            hasProjects = true,
            archived = false,
            disabled = false,
            openIssues = 5,
            isTemplate = false,
            owner = NetworkUser(
                id = 1,
                login = "sample",
                avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
            ),
        )

        val serialized =
            jsonFormat.encodeToString(NetworkRepository.serializer(), networkRepository)

        val expectedJson = """
            {
                "id": 1,
                "node_id" : "MDEwOlJlcG9zaXRvcnkzMjAwNDEwNzE=",
                "name": "Sample Repo",
                "full_name": "sample/sample-repo",
                "private": false,
                "description": "A sample repository",
                "fork": false,
                "size": 1024,
                "stargazers_count": 150,
                "watchers_count": 75,
                "forks_count": 10,
                "language": "Kotlin",
                "has_issues": true,
                "has_projects": true,
                "archived": false,
                "disabled": false,
                "open_issues_count": 5,
                "is_template": false,
                "owner": {
                    "id": 1,
                    "login": "sample",
                    "avatar_url": "https://avatars.githubusercontent.com/u/1?v=4"
                }
            }
        """.trimIndent().replace("\n", "").replace(" ", "")

        assertEquals(expectedJson, serialized.replace("\n", "").replace(" ", ""))
    }

    @Test
    fun `test deserialization of NetworkRepository`() {
        val repositoryJson = """
            {
                "id": 2,
                "node_id" : "MDEwOlJlcG9zaXRvcnkzMjAwNDEwNzE=",
                "name": "Another Repo",
                "full_name": "another/another-repo",
                "private": true,
                "description": "Another repository example",
                "fork": true,
                "size": 2048,
                "stargazers_count": 300,
                "watchers_count": 150,
                "forks_count": 20,
                "language": "Java",
                "has_issues": false,
                "has_projects": false,
                "archived": true,
                "disabled": true,
                "open_issues_count": 10,
                "is_template": false,
                "owner": {
                    "id": 1,
                    "login": "sample",
                    "avatar_url": "https://avatars.githubusercontent.com/u/1?v=4"
                }
            }
        """.trimIndent()

        val repository = jsonFormat.decodeFromString<NetworkRepository>(repositoryJson)

        // Assert deserialized values
        assertNotNull(repository)
        assertEquals(2, repository.id)
        assertEquals("MDEwOlJlcG9zaXRvcnkzMjAwNDEwNzE=", repository.repositoryId)
        assertEquals("Another Repo", repository.name)
        assertEquals("another/another-repo", repository.fullName)
        assertEquals(true, repository.isPrivate)
        assertEquals("Another repository example", repository.description)
        assertEquals(true, repository.isFork)
        assertEquals(2048, repository.size)
        assertEquals(300, repository.stars)
        assertEquals(150, repository.watchers)
        assertEquals(20, repository.forks)
        assertEquals("Java", repository.language)
        assertEquals(false, repository.hasIssues)
        assertEquals(false, repository.hasProjects)
        assertEquals(true, repository.archived)
        assertEquals(true, repository.disabled)
        assertEquals(10, repository.openIssues)
        assertEquals(false, repository.isTemplate)
        assertEquals(1, repository.owner?.id)
        assertEquals("sample", repository.owner?.login)
        assertEquals("https://avatars.githubusercontent.com/u/1?v=4", repository.owner?.avatarUrl)
    }

    @Test
    fun `test default values in NetworkRepository`() {
        val repositoryJson = """
            {
                "id": 3,
                "node_id" : "MDEwOlJlcG9zaXRvcnkzMjAwNDEwNzE=",
                "name": "Minimal Repo"
            }
        """.trimIndent()

        val repository = jsonFormat.decodeFromString<NetworkRepository>(repositoryJson)

        assertNotNull(repository)
        assertEquals(3, repository.id)
        assertEquals("MDEwOlJlcG9zaXRvcnkzMjAwNDEwNzE=", repository.repositoryId)
        assertEquals("Minimal Repo", repository.name)
        assertEquals(null, repository.fullName)
        assertEquals(null, repository.isPrivate)
        assertEquals(null, repository.description)
        assertEquals(null, repository.isFork)
        assertEquals(null, repository.size)
        assertEquals(null, repository.stars)
        assertEquals(null, repository.watchers)
        assertEquals(null, repository.forks)
        assertEquals(null, repository.language)
        assertEquals(null, repository.hasIssues)
        assertEquals(null, repository.hasProjects)
        assertEquals(null, repository.archived)
        assertEquals(null, repository.disabled)
        assertEquals(null, repository.openIssues)
        assertEquals(null, repository.isTemplate)
    }

    @Test
    fun `map network repository model to dao entity`() {
        val networkRepository = NetworkRepository(
            id = 1,
            repositoryId = "MDEwOlJlcG9zaXRvcnkzMjAwNDEwNzE=",
            name = "Sample Repo",
            fullName = "sample/sample-repo",
            isPrivate = false,
            description = "A sample repository",
            isFork = false,
            size = 1024,
            stars = 150,
            watchers = 75,
            forks = 10,
            language = "Kotlin",
            hasIssues = true,
            hasProjects = true,
            archived = false,
            disabled = false,
            openIssues = 5,
            isTemplate = false,
            owner = NetworkUser(
                id = 1,
                login = "sample",
                avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
            ),
        )

        val expectedRepositoryEntity = RepositoryEntity(
            id = 1,
            repositoryId = "MDEwOlJlcG9zaXRvcnkzMjAwNDEwNzE=",
            name = "Sample Repo",
            fullName = "sample/sample-repo",
            isPrivate = false,
            description = "A sample repository",
            isFork = false,
            size = 1024,
            stars = 150,
            watchers = 75,
            forks = 10,
            language = "Kotlin",
            hasIssues = true,
            hasProjects = true,
            archived = false,
            disabled = false,
            openIssues = 5,
            isTemplate = false,
            owner = UserEntity(
                id = 1,
                login = "sample",
                avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
            ),
        )

        assertEquals(expectedRepositoryEntity, networkRepository.asEntity())
    }
}
