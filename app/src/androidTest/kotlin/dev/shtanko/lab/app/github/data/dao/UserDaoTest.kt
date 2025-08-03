package dev.shtanko.lab.app.github.data.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.shtanko.lab.app.github.data.db.DatabaseTest
import dev.shtanko.lab.app.github.data.db.entity.UserEntity
import dev.shtanko.lab.app.github.data.db.entity.UserEntityFull
import dev.shtanko.lab.app.utils.toDate
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

    @Test
    fun insertFullUser_insertsFullUserSuccessfully() = runTest {
        userDao.insertFullUser(entity = mockFullUser)
        val fullUserFromDb = userDao.getFullUser()
        assertNotNull(fullUserFromDb)
        assertEquals(mockFullUser, fullUserFromDb)
    }

    @Test
    fun getFullUserByLogin_returnsFullUserSuccessfully() = runTest {
        userDao.insertFullUser(entity = mockFullUser)
        require(mockFullUser.login != null) {
            "login should not be null"
        }
        val fullUserFromDb = userDao.getFullUserByLogin(login = mockFullUser.login)
        assertNotNull(fullUserFromDb)
        assertEquals(mockFullUser, fullUserFromDb)
    }

    @Test
    fun clearAllFullUsers_deletesFullUsersSuccessfully() = runTest {
        userDao.insertFullUser(entity = mockFullUser)
        val fullUserFromDb = userDao.getFullUser()
        assertNotNull(fullUserFromDb)
        userDao.clearAllFullUsers()
        val deletedFullUserFromDb = userDao.getFullUser()
        assertTrue(deletedFullUserFromDb == null)
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

    private val mockFullUser = UserEntityFull(
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
}
