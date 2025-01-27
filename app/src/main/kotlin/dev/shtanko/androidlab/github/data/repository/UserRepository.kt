package dev.shtanko.androidlab.github.data.repository

import com.skydoves.sandwich.isSuccess
import com.skydoves.sandwich.suspendOnSuccess
import dev.shtanko.androidlab.github.data.db.dao.UserDao
import dev.shtanko.androidlab.github.data.db.entity.UserEntity
import dev.shtanko.androidlab.github.data.db.entity.asExternalModel
import dev.shtanko.androidlab.github.data.di.IoDispatcher
import dev.shtanko.androidlab.github.data.model.NetworkUser
import dev.shtanko.androidlab.github.data.model.asEntity
import dev.shtanko.androidlab.github.data.service.UserService
import dev.shtanko.androidlab.github.presentation.model.UserFullResource
import dev.shtanko.androidlab.github.presentation.model.UserResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface UserRepository {
    fun fetchUsers(force: Boolean = false): Flow<Result<List<UserResource>>>

    fun getFullUser(force: Boolean = false, login: String): Flow<Result<UserFullResource>>
}

class OfflineFirstUserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userService: UserService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : UserRepository {
    override fun fetchUsers(force: Boolean): Flow<Result<List<UserResource>>> = flow {
        val cachedUsers = userDao.getUsers()?.map(UserEntity::asExternalModel)

        if (cachedUsers != null && cachedUsers.isNotEmpty() && !force) {
            emit(Result.success(cachedUsers))
        } else {
            val apiResponse = userService.fetchUsers()
            if (apiResponse.isSuccess) {
                apiResponse.suspendOnSuccess {
                    val userEntities = data.map(NetworkUser::asEntity)
                    userDao.insertAll(userEntities)
                    val finalUsers = userDao.getUsers()?.map(UserEntity::asExternalModel)
                        ?: userEntities.map { it.asExternalModel() }
                    emit(Result.success(finalUsers))
                }
            } else {
                emit(Result.failure(Error("Error fetching users from API")))
            }
        }
    }.flowOn(ioDispatcher)

    override fun getFullUser(force: Boolean, login: String): Flow<Result<UserFullResource>> = flow {
        val cachedUser = userDao.getFullUserByLogin(login)?.asExternalModel()

        if (cachedUser != null && !force) {
            emit(Result.success(cachedUser))
        } else {
            val apiResponse = userService.getFullUser(login)
            if (apiResponse.isSuccess) {
                apiResponse.suspendOnSuccess {
                    val userEntity = data.asEntity()
                    userDao.insertFullUser(userEntity)
                    val finalUser = userDao.getFullUserByLogin(login)?.asExternalModel()
                        ?: userEntity.asExternalModel()
                    emit(Result.success(finalUser))
                }
            } else {
                emit(Result.failure(Error("Error fetching full user $login from API")))
            }
        }
    }.flowOn(ioDispatcher)
}
