package com.eyepax.newsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.eyepax.newsapp.model.Article
import com.eyepax.newsapp.model.User

@Database(

    entities = [Article::class, User::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class NewsAppDatabase: RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao
    abstract fun getUserDao(): UserDao

    companion object {
        @Volatile
        private var instance: NewsAppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                NewsAppDatabase::class.java,
                "news_app_db.db"
            ).build()
    }
}