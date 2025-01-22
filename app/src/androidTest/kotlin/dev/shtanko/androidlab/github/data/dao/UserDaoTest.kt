package dev.shtanko.androidlab.github.data.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.shtanko.androidlab.github.data.db.DatabaseTest
import dev.shtanko.androidlab.github.data.db.entity.UserEntity
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest : DatabaseTest() {
    @Test
    fun insertUsers_insertsUsersSuccessfully() = runTest {
        userDao.insertAll(users = mockUsers)
        val usersFromDb = userDao.getUsers()
        assertNotNull(usersFromDb)
        assertEquals(2, usersFromDb?.size)
        assertEquals(mockUsers, usersFromDb)
    }

    @Test
    fun deleteUsers_deletesUsersSuccessfully() = runTest {
        userDao.insertAll(users = mockUsers)
        val usersFromDb = userDao.getUsers()
        assertNotNull(usersFromDb)
        assertEquals(mockUsers, usersFromDb)
        userDao.clearAllUsers()
        val deletedUsersFromDb = userDao.getUsers()
        assertTrue(deletedUsersFromDb?.isEmpty() == true)
    }

    private val mockUsers = listOf(
        UserEntity(
            id = 1,
            login = "alex",
        ),
        UserEntity(
            id = 2,
            login = "oleksii",
        ),
    )
}
