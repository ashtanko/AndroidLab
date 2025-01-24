package dev.shtanko.androidlab.github.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.shtanko.androidlab.github.data.db.entity.RepositoryEntity
import dev.shtanko.androidlab.github.data.db.entity.RepositoryRemoteKeysEntity

@Dao
interface RepositoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<RepositoryEntity>)

    @Query("SELECT * FROM repos ORDER BY page")
    fun getRepositories(): PagingSource<Int, RepositoryEntity>

    @Query("DELETE FROM repos")
    suspend fun clearAll()
}

@Dao
interface RepositoryRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RepositoryRemoteKeysEntity>)

    @Query("SELECT * FROM repos_remote_key WHERE repository_id = :id")
    suspend fun getRemoteKeyById(id: String): RepositoryRemoteKeysEntity?

    @Query("Select created_at FROM repos_remote_key ORDER BY created_at DESC LIMIT 1")
    suspend fun getCreationTime(): Long?

    @Query("DELETE FROM repos_remote_key")
    suspend fun clearRemoteKeys()
}
