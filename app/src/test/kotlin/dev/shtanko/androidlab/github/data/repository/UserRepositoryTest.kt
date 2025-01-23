package dev.shtanko.androidlab.github.data.repository

import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.responseOf
import dev.shtanko.androidlab.github.data.db.dao.UserDao
import dev.shtanko.androidlab.github.data.db.entity.UserEntity
import dev.shtanko.androidlab.github.data.model.NetworkUser
import dev.shtanko.androidlab.github.data.model.asEntity
import dev.shtanko.androidlab.github.data.service.UserService
import dev.shtanko.androidlab.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Rule
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

    val mockUserEntities = listOf(
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
}
