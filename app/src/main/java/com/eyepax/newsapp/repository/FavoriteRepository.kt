package com.eyepax.newsapp.repository

import com.eyepax.newsapp.db.NewsAppDatabase
import com.eyepax.newsapp.model.Article
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val db: NewsAppDatabase
) {

    suspend fun insert(article: Article) = db.getArticleDao().insert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()
}