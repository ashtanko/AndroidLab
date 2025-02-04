package dev.shtanko.androidlab.movies.data.db.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.shtanko.androidlab.github.data.db.GithubDatabase
import javax.inject.Named
import javax.inject.Singleton

private const val MOVIES_DATABASE_NAME = "movies-db"

@Module
@InstallIn(SingletonComponent::class)
object MoviesDatabaseModule {
    @Provides
    @Singleton
    @Named("MoviesDatabase")
    fun provideMoviesDatabase(
        @ApplicationContext context: Context,
    ): GithubDatabase = Room
        .databaseBuilder(
            context,
            GithubDatabase::class.java,
            MOVIES_DATABASE_NAME,
        )
        .fallbackToDestructiveMigration()
        .build()
}
