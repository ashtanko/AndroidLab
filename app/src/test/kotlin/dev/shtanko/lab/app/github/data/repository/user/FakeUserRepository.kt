package dev.shtanko.lab.app.github.data.repository.user

import dev.shtanko.lab.app.github.data.repository.UserRepository
import dev.shtanko.lab.app.github.presentation.model.UserFullResource
import dev.shtanko.lab.app.github.presentation.model.UserResource
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class FakeUserRepository @Inject constructor() : UserRepository {

    private val userResourcesFlow: MutableSharedFlow<Result<List<UserResource>>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val fullUserResourceFlow: MutableSharedFlow<Result<UserFullResource>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun fetchUsers(force: Boolean): Flow<Result<List<UserResource>>> = userResourcesFlow

    override fun getFullUser(
        force: Boolean,
        login: String,
    ): Flow<Result<UserFullResource>> = fullUserResourceFlow

    fun emitUserResources(newsResources: Result<List<UserResource>>) {
        userResourcesFlow.tryEmit(newsResources)
    }

    fun emitFullUserResource(newsResource: Result<UserFullResource>) {
        fullUserResourceFlow.tryEmit(newsResource)
    }
}
