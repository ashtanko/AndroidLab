@file:OptIn(ExperimentalPagingApi::class)

package dev.shtanko.androidlab.github.data.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.skydoves.sandwich.getOrNull
import com.skydoves.sandwich.isException
import com.skydoves.sandwich.isSuccess
import dev.shtanko.androidlab.github.data.db.GithubDatabase
import dev.shtanko.androidlab.github.data.db.entity.RepositoryEntity
import dev.shtanko.androidlab.github.data.db.entity.RepositoryRemoteKeysEntity
import dev.shtanko.androidlab.github.data.model.NetworkRepository
import dev.shtanko.androidlab.github.data.model.asEntity
import dev.shtanko.androidlab.github.data.service.RepositoryService
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

class RepositoryMediator(
    private val service: RepositoryService,
    private val database: GithubDatabase,
    private val cacheTimeout: Long = TimeUnit.MILLISECONDS.convert(10, TimeUnit.HOURS),
    private val username: String = "google",
) : RemoteMediator<Int, RepositoryEntity>() {

    override suspend fun initialize(): InitializeAction {
        val users = database.repositoryRemoteKeysDao().getUsers()
        val isUserExist = users?.contains(username) == true
        val creationTime = database.repositoryRemoteKeysDao().getCreationTime() ?: 0
        val currentTime = System.currentTimeMillis()
        val timeDiff = currentTime - creationTime
        return if (timeDiff < cacheTimeout && isUserExist) {
            // Cached data is up-to-date, so there is no need to re-fetch
            // from the network.
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            // Need to refresh cached data from network; returning
            // LAUNCH_INITIAL_REFRESH here will also block RemoteMediator's
            // APPEND and PREPEND from running until REFRESH succeeds.
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    @Suppress("CognitiveComplexMethod")
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RepositoryEntity>,
    ): MediatorResult {
        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> return pageKeyData
            else -> pageKeyData as Int
        }

        try {
            val perPage = state.config.pageSize
            val response = service.fetchRepos(user = username, page = page, perPage = perPage)

            Log.d(
                "RepositoryMediator",
                "username: $username response: $response ${response.isException}",
            )

            if (response.isSuccess) {
                val responseList = response.getOrNull() ?: emptyList()
                val entities = responseList.map(NetworkRepository::asEntity)
                val isEndOfList = responseList.isEmpty()

                Log.d(
                    "RepositoryMediator",
                    "username: $username $entities isEndOfList: $isEndOfList",
                )

                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        database.repositoryRemoteKeysDao().clearRemoteKeys()
                        database.repositoryDao().clearAll()
                    }
                    val prevKey = if (page > 1) page - 1 else null
                    val nextKey = if (isEndOfList) null else page + 1

                    val remoteKeys = entities.map {
                        RepositoryRemoteKeysEntity(
                            repositoryId = it.repositoryId,
                            user = it.owner?.login.orEmpty(),
                            prevKey = prevKey,
                            currentPage = page,
                            nextKey = nextKey,
                        )
                    }
                    database.repositoryRemoteKeysDao().insertAll(remoteKeys)
                    database.repositoryDao()
                        .insertAll(
                            entities.map { entity -> entity.copy(page = page) },
                        )
                }

                return MediatorResult.Success(endOfPaginationReached = isEndOfList)
            } else {
                return MediatorResult.Error(Error("error on fetching repositories"))
            }
        } catch (error: IOException) {
            return MediatorResult.Error(error)
        } catch (error: HttpException) {
            return MediatorResult.Error(error)
        } catch (error: Error) {
            return MediatorResult.Error(error)
        }
    }

    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, RepositoryEntity>,
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = false,
                )
            }

            else -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, RepositoryEntity>): RepositoryRemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.repositoryId?.let { id ->
                database.repositoryRemoteKeysDao().getRemoteKeyById(id)
            }
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, RepositoryEntity>): RepositoryRemoteKeysEntity? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { repo ->
                database.repositoryRemoteKeysDao()
                    .getRemoteKeyById(repo.repositoryId)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, RepositoryEntity>): RepositoryRemoteKeysEntity? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { repo ->
                database.repositoryRemoteKeysDao()
                    .getRemoteKeyById(repo.repositoryId)
            }
    }
}
