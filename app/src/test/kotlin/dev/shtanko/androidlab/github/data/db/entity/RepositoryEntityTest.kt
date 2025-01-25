package dev.shtanko.androidlab.github.data.db.entity

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotSame
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class RepositoryEntityTest {
    @Test
    fun `verify default values in RepositoryEntity`() {
        val repositoryEntity = RepositoryEntity(repositoryId = "123")

        assertEquals(0, repositoryEntity.id)
        assertEquals("123", repositoryEntity.repositoryId)
        assertNull(repositoryEntity.name)
        assertNull(repositoryEntity.fullName)
        assertNull(repositoryEntity.isPrivate)
        assertNull(repositoryEntity.description)
        assertNull(repositoryEntity.isFork)
        assertNull(repositoryEntity.size)
        assertNull(repositoryEntity.stars)
        assertNull(repositoryEntity.watchers)
        assertNull(repositoryEntity.forks)
        assertNull(repositoryEntity.language)
        assertNull(repositoryEntity.hasIssues)
        assertNull(repositoryEntity.hasProjects)
        assertNull(repositoryEntity.archived)
        assertNull(repositoryEntity.disabled)
        assertNull(repositoryEntity.openIssues)
        assertNull(repositoryEntity.isTemplate)
        assertEquals(0, repositoryEntity.page)
    }

    @Test
    fun `verify asExternalModel conversion`() {
        val repositoryEntity = RepositoryEntity(
            id = 1,
            repositoryId = "repo123",
            name = "TestRepo",
            fullName = "Test/Repo",
            isPrivate = true,
            description = "A test repository",
            isFork = false,
            size = 150,
            stars = 42,
            watchers = 10,
            forks = 5,
            language = "Kotlin",
            hasIssues = true,
            hasProjects = false,
            archived = false,
            disabled = false,
            openIssues = 3,
            isTemplate = false,
            owner = UserEntity(
                id = 1,
                login = "sample",
                avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
            ),
        )

        val externalModel = repositoryEntity.asExternalModel()

        assertEquals(repositoryEntity.id, externalModel.id)
        assertEquals(repositoryEntity.repositoryId, externalModel.repositoryId)
        assertEquals(repositoryEntity.name, externalModel.name)
        assertEquals(repositoryEntity.fullName, externalModel.fullName)
        assertEquals(repositoryEntity.isPrivate, externalModel.isPrivate)
        assertEquals(repositoryEntity.description, externalModel.description)
        assertEquals(repositoryEntity.isFork, externalModel.isFork)
        assertEquals(repositoryEntity.size, externalModel.size)
        assertEquals(repositoryEntity.stars, externalModel.stars)
        assertEquals(repositoryEntity.watchers, externalModel.watchers)
        assertEquals(repositoryEntity.forks, externalModel.forks)
        assertEquals(repositoryEntity.language, externalModel.language)
        assertEquals(repositoryEntity.hasIssues, externalModel.hasIssues)
        assertEquals(repositoryEntity.hasProjects, externalModel.hasProjects)
        assertEquals(repositoryEntity.archived, externalModel.archived)
        assertEquals(repositoryEntity.disabled, externalModel.disabled)
        assertEquals(repositoryEntity.openIssues, externalModel.openIssues)
        assertEquals(repositoryEntity.isTemplate, externalModel.isTemplate)
        assertEquals(repositoryEntity.owner?.id, externalModel.owner?.id)
        assertEquals(repositoryEntity.owner?.login, externalModel.owner?.login)
        assertEquals(repositoryEntity.owner?.avatarUrl, externalModel.owner?.avatarUrl)
    }

    @Test
    fun `verify equality and hashCode`() {
        val repo1 = RepositoryEntity(repositoryId = "repo123", name = "Repo")
        val repo2 = RepositoryEntity(repositoryId = "repo123", name = "Repo")
        val repo3 = RepositoryEntity(repositoryId = "repo124", name = "Another Repo")

        assertEquals(repo1, repo2)
        assertNotSame(
            repo1,
            repo3,
            "unexpected and actual should not be the same objects in memory",
        )
        assertEquals(repo1.hashCode(), repo2.hashCode())
        assertNotEquals(repo1.hashCode(), repo3.hashCode())
    }
}
