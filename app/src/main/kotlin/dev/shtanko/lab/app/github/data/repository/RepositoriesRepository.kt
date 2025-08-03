package dev.shtanko.lab.app.github.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import dev.shtanko.lab.app.github.data.db.GithubDatabase
import dev.shtanko.lab.app.github.data.db.entity.RepositoryEntity
import dev.shtanko.lab.app.github.data.db.entity.asExternalModel
import dev.shtanko.lab.app.github.data.di.IoDispatcher
import dev.shtanko.lab.app.github.data.mediator.RepositoryMediator
import dev.shtanko.lab.app.github.data.service.RepositoryService
import dev.shtanko.lab.app.github.presentation.model.RepositoryResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface RepositoriesRepository {
    fun getRepos(username: String = "google"): Flow<PagingData<RepositoryResource>>
}

@OptIn(ExperimentalPagingApi::class)
class OfflineFirstRepositoriesRepository @Inject constructor(
    private val service: RepositoryService,
    private val db: GithubDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : RepositoriesRepository {
    override fun getRepos(username: String): Flow<PagingData<RepositoryResource>> = Pager(
        config = PagingConfig(
            initialLoadSize = 20,
            pageSize = 20,
            prefetchDistance = 1,
            maxSize = PagingConfig.MAX_SIZE_UNBOUNDED,
            jumpThreshold = Int.MIN_VALUE,
            enablePlaceholders = true,
        ),
        pagingSourceFactory = {
            db.repositoryDao().getRepositories()
        },
        remoteMediator = RepositoryMediator(
            service = service,
            database = db,
            username = username,
        ),
    ).flow.map { pagingData ->
        pagingData.map(RepositoryEntity::asExternalModel)
    }.flowOn(ioDispatcher)
}
