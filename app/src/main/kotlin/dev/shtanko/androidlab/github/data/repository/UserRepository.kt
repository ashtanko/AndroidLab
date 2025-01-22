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
import dev.shtanko.androidlab.github.presentation.model.UserResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface UserRepository {
    fun fetchUsers(force: Boolean = false): Flow<Result<List<UserResource>>>
}

class OfflineFirstUserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userService: UserService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : UserRepository {
    override fun fetchUsers(force: Boolean): Flow<Result<List<UserResource>>> = flow {
        val users = userDao.getUsers()?.map(UserEntity::asExternalModel)
        if (users != null && users.isNotEmpty() && !force) {
            emit(Result.success(users))
        } else {
            val response = userService.fetchUsers()
            if (response.isSuccess) {
                response.suspendOnSuccess {
                    val mappedResponse = data.map(NetworkUser::asEntity)
                    userDao.insertAll(mappedResponse)
                    val final = userDao.getUsers()?.map(UserEntity::asExternalModel)
                        ?: mappedResponse.map { it.asExternalModel() }
                    emit(Result.success(final))
                }
            } else {
                emit(Result.failure(Error("Error fetching users from API")))
            }
        }
    }.flowOn(dispatcher)
}
