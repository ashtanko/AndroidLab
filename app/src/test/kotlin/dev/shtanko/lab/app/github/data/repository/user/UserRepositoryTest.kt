package dev.shtanko.lab.app.github.data.repository.user

import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.responseOf
import dev.shtanko.lab.app.github.data.db.dao.UserDao
import dev.shtanko.lab.app.github.data.db.entity.UserEntity
import dev.shtanko.lab.app.github.data.db.entity.UserEntityFull
import dev.shtanko.lab.app.github.data.db.entity.asExternalModel
import dev.shtanko.lab.app.github.data.model.NetworkUser
import dev.shtanko.lab.app.github.data.model.NetworkUserFull
import dev.shtanko.lab.app.github.data.model.asEntity
import dev.shtanko.lab.app.github.data.repository.OfflineFirstUserRepository
import dev.shtanko.lab.app.github.data.repository.UserRepository
import dev.shtanko.lab.app.github.data.service.UserService
import dev.shtanko.lab.app.util.MainDispatcherRule
import dev.shtanko.lab.app.utils.toDate
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import retrofit2.Response

class UserRepositoryTest {
    private lateinit var repository: UserRepository
    private val dao: UserDao = mock()
    private val service: UserService = mock()

    @get:Rule
    val coroutinesRule = MainDispatcherRule()

    @BeforeEach
    fun setup() {
        repository = OfflineFirstUserRepository(
            userService = service,
            userDao = dao,
            ioDispatcher = coroutinesRule.testDispatcher,
        )
    }

    @Test
    fun `GIVEN full user present in DAO WHEN getFullUser THEN return data from storage and don't call backend`() =
        runTest {
            val mockUserLogin = "testUser"
            whenever(dao.getFullUserByLogin(mockUserLogin)).thenReturn(mockFullUserEntity)
            repository.getFullUser(login = mockUserLogin).test {
                val actualItem = awaitItem()
                assertNotNull(actualItem)
                assertTrue(actualItem.isSuccess)
                assertEquals(mockFullUserEntity.asExternalModel(), actualItem.getOrNull())
                awaitComplete()
            }
            verify(dao, times(1)).getFullUserByLogin(mockUserLogin)
            verify(service, never()).getFullUser(mockUserLogin)
        }

    @Test
    fun `GIVEN full user present in DAO WHEN getFullUser force THEN return data from backend and update storage`() =
        runTest {
            val mockUserLogin = "testUser"
            whenever(dao.getFullUserByLogin(mockUserLogin)).thenReturn(mockFullUserEntity)
            whenever(service.getFullUser(mockUserLogin)).thenReturn(
                ApiResponse.responseOf {
                    Response.success(
                        mockNetworkUserFull,
                    )
                },
            )
            repository.getFullUser(login = mockUserLogin, force = true).test {
                val actualItem = awaitItem()
                assertNotNull(actualItem)
                assertTrue(actualItem.isSuccess)
                assertEquals(mockFullUserEntity.asExternalModel(), actualItem.getOrNull())
                awaitComplete()
            }
            verify(dao, times(2)).getFullUserByLogin(mockUserLogin)
            verify(dao, times(1)).insertFullUser(mockFullUserEntity)
            verify(service, times(1)).getFullUser(mockUserLogin)
        }

    @Test
    fun `GIVEN no full user present in DAO WHEN getFullUser THEN fetch data from backend`() =
        runTest {
            val mockUserLogin = "testUser"
            val mockApiResponse = ApiResponse.responseOf {
                Response.success(
                    mockNetworkUserFull,
                )
            }

            whenever(dao.getFullUser()).thenReturn(null)
            whenever(service.getFullUser(mockUserLogin)).thenReturn(mockApiResponse)
            repository.getFullUser(login = mockUserLogin).test {
                val actualItem = awaitItem()
                assertNotNull(actualItem)
                assertTrue(actualItem.isSuccess)
                assertEquals(mockFullUserEntity.asExternalModel(), actualItem.getOrNull())
                awaitComplete()
            }
            verify(dao, times(2)).getFullUserByLogin(mockUserLogin)
            verify(dao, times(1)).insertFullUser(mockNetworkUserFull.asEntity())
            verify(service, times(1)).getFullUser(mockUserLogin)
        }

    @Test
    fun `GIVEN no full user present in DAO WHEN getFullUser THEN return an error from backend`() =
        runTest {
            val mockUserLogin = "testUser"
            whenever(dao.getFullUser()).thenReturn(null)
            whenever(service.getFullUser(user = mockUserLogin)).thenReturn(
                ApiResponse.responseOf {
                    Response.error(
                        400,
                        "No data found".toResponseBody(null),
                    )
                },
            )
            repository.getFullUser(login = mockUserLogin).test {
                val actualItem = awaitItem()
                assertTrue(actualItem.isFailure)
                awaitComplete()
            }
            verify(dao, times(1)).getFullUserByLogin(mockUserLogin)
            verify(service, times(1)).getFullUser(mockUserLogin)
            verify(dao, never()).insertFullUser(mockNetworkUserFull.asEntity())
        }

    @Test
    fun `GIVEN users present in DAO WHEN fetchUsers THEN return data from storage and don't call backend`() =
        runTest {
            whenever(dao.getUsers()).thenReturn(mockUserEntities)
            repository.fetchUsers().test {
                val actualItem = awaitItem()
                assertNotNull(actualItem)
                assertTrue(actualItem.isSuccess)
                awaitComplete()
            }
            verify(dao, times(1)).getUsers()
            verify(service, never()).fetchUsers()
        }

    @Test
    fun `GIVEN users present in DAO WHEN fetchUsers force THEN return data from backend and update storage`() =
        runTest {
            val mockApiResponse = ApiResponse.responseOf {
                Response.success(
                    mockNetworkUsers,
                )
            }
            whenever(dao.getUsers()).thenReturn(mockUserEntities)
            whenever(service.fetchUsers()).thenReturn(mockApiResponse)
            repository.fetchUsers(force = true).test {
                val actualItem = awaitItem()
                assertNotNull(actualItem)
                assertTrue(actualItem.isSuccess)
                awaitComplete()
            }
            verify(dao, times(2)).getUsers()
            verify(service, times(1)).fetchUsers()
            verify(dao, times(1)).insertAll(mockNetworkUsers.map(NetworkUser::asEntity))
        }

    @Test
    fun `GIVEN empty list present in DAO WHEN fetchUsers THEN fetch data from backend and save to db`() =
        runTest {
            val mockApiResponse = ApiResponse.responseOf {
                Response.success(
                    mockNetworkUsers,
                )
            }
            whenever(dao.getUsers()).thenReturn(emptyList<UserEntity>())
            whenever(service.fetchUsers()).thenReturn(mockApiResponse)
            repository.fetchUsers().test {
                val actualItem = awaitItem()
                assertNotNull(actualItem)
                assertTrue(actualItem.isSuccess)
                awaitComplete()
            }
            verify(dao, times(2)).getUsers()
            verify(dao, times(1)).insertAll(mockNetworkUsers.map(NetworkUser::asEntity))
            verify(service, times(1)).fetchUsers()
        }

    @Test
    fun `GIVEN data is not present in DAO WHEN fetchUsers THEN fetch data from backend`() =
        runTest {
            val mockApiResponse = ApiResponse.responseOf {
                Response.success(
                    mockNetworkUsers,
                )
            }
            whenever(dao.getUsers()).thenReturn(null)
            whenever(service.fetchUsers()).thenReturn(mockApiResponse)
            repository.fetchUsers().test {
                val actualItem = awaitItem()
                assertNotNull(actualItem)
                assertTrue(actualItem.isSuccess)
                awaitComplete()
            }
            verify(dao, times(2)).getUsers()
            verify(dao, times(1)).insertAll(mockNetworkUsers.map(NetworkUser::asEntity))
            verify(service, times(1)).fetchUsers()
        }

    @Test
    fun `GIVEN users are not present in DAO WHEN fetchUsers THEN return an error from backend`() =
        runTest {
            whenever(dao.getUsers()).thenReturn(null)
            whenever(service.fetchUsers()).thenReturn(
                ApiResponse.responseOf {
                    Response.error(
                        400,
                        "No data found".toResponseBody(null),
                    )
                },
            )
            repository.fetchUsers().test {
                val actualItem = awaitItem()
                assertTrue(actualItem.isFailure)
                awaitComplete()
            }
            verify(dao, times(1)).getUsers()
            verify(service, times(1)).fetchUsers()
            verify(dao, never()).insertAll(mockNetworkUsers.map(NetworkUser::asEntity))
        }

    private val mockUserEntities = listOf(
        UserEntity(
            id = 1,
            login = "alex",
        ),
        UserEntity(
            id = 2,
            login = "oleksii",
        ),
    )

    private val mockNetworkUsers = listOf(
        NetworkUser(
            id = 1,
            login = "alex",
        ),
        NetworkUser(
            id = 2,
            login = "oleksii",
        ),
    )

    private val mockFullUserEntity = UserEntityFull(
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

    private val mockNetworkUserFull = NetworkUserFull(
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
}
