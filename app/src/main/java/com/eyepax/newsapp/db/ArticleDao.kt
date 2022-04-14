package com.eyepax.newsapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.eyepax.newsapp.model.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

}