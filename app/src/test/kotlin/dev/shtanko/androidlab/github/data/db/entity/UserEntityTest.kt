package dev.shtanko.androidlab.github.data.db.entity

import dev.shtanko.androidlab.utils.toDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UserEntityTest {
    @Test
    fun `test mapping UserEntity to UserResource`() {
        val userEntity = UserEntity(
            id = 1,
            login = "testUser",
            avatarUrl = "http://example.com/avatar.png",
        )

        val userResource = userEntity.asExternalModel()

        assertEquals(userEntity.id, userResource.id)
        assertEquals(userEntity.login, userResource.login)
        assertEquals(userEntity.avatarUrl, userResource.avatarUrl)
    }

    @Test
    fun `test mapping UserFullEntity to UserFullResource`() {
        val userFullEntity = UserEntityFull(
            id = 1,
            login = "testUser",
            avatarUrl = "http://example.com/avatar.png",
            name = "Test User",
            company = "Test Company",
            blog = "http://example.com",
            location = "Test Location",
            hireable = true,
            bio = "Test Bio",
            twitter = "testUser",
            publicReposCount = 10,
            publicGistsCount = 5,
            followers = 100,
            following = 50,
            createdAt = "2021-01-01T00:00:00Z".toDate(),
        )

        val userFullResource = userFullEntity.asExternalModel()

        assertEquals(userFullEntity.id, userFullResource.id)
        assertEquals(userFullEntity.login, userFullResource.login)
        assertEquals(userFullEntity.avatarUrl, userFullResource.avatarUrl)
        assertEquals(userFullEntity.name, userFullResource.name)
        assertEquals(userFullEntity.company, userFullResource.company)
        assertEquals(userFullEntity.blog, userFullResource.blog)
        assertEquals(userFullEntity.location, userFullResource.location)
        assertEquals(userFullEntity.hireable, userFullResource.hireable)
        assertEquals(userFullEntity.bio, userFullResource.bio)
        assertEquals(userFullEntity.twitter, userFullResource.twitter)
        assertEquals(userFullEntity.publicReposCount, userFullResource.publicReposCount)
        assertEquals(userFullEntity.publicGistsCount, userFullResource.publicGistsCount)
        assertEquals(userFullEntity.followers, userFullResource.followers)
        assertEquals(userFullEntity.following, userFullResource.following)
        assertEquals(userFullEntity.createdAt, userFullResource.createdAt)
    }
}
