package com.eyepax.newsapp.model

import com.eyepax.newsapp.model.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)