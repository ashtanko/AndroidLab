package dev.shtanko.androidlab.github.data.db.entity

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MapperTest {
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
}
