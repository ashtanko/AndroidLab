package dev.shtanko.androidlab.github.data.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dev.shtanko.androidlab.github.data.db.dao.UserDao
import org.junit.After
import org.junit.Before

abstract class DatabaseTest {
    private lateinit var db: GithubDatabase
    lateinit var userDao: UserDao

    @Before
    fun setup() {
        db = run {
            val context = ApplicationProvider.getApplicationContext<Context>()
            Room.inMemoryDatabaseBuilder(
                context,
                GithubDatabase::class.java,
            ).build()
        }
        userDao = db.userDao()
    }

    @After
    fun teardown() = db.close()
}
