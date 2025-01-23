package dev.shtanko.androidlab.github.data.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NetworkMapperTest {
    @Test
    fun `test NetworkUser to UserEntity mapping`() {
        val networkUser = NetworkUser(
            id = 1,
            login = "test_login",
            avatarUrl = "test_avatar_url",
        )

        val userEntity = networkUser.asEntity()

        assertEquals(networkUser.id, userEntity.id)
        assertEquals(networkUser.login, userEntity.login)
        assertEquals(networkUser.avatarUrl, userEntity.avatarUrl)
    }
}
