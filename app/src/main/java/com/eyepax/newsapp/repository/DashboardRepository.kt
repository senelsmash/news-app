package com.eyepax.newsapp.repository

import com.eyepax.newsapp.network.NewsApiService
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val newsApi: NewsApiService
)  {

    suspend fun getTopHeadlines(countryCode: String) =
        newsApi.getTopHeadlines(countryCode)

}