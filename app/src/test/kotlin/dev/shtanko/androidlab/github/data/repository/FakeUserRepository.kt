package dev.shtanko.androidlab.github.data.repository

import dev.shtanko.androidlab.github.presentation.model.UserResource
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class FakeUserRepository @Inject constructor() : UserRepository {

    private val userResourcesFlow: MutableSharedFlow<Result<List<UserResource>>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun fetchUsers(force: Boolean): Flow<Result<List<UserResource>>> = userResourcesFlow

    fun emitUserResources(newsResources: Result<List<UserResource>>) {
        userResourcesFlow.tryEmit(newsResources)
    }
}
