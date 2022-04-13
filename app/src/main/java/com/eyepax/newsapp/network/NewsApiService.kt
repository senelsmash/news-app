package com.eyepax.newsapp.network

import com.eyepax.newsapp.AppConstant
import com.eyepax.newsapp.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country")
        countryCode: String = "us",
        @Query("apiKey")
        apiKey: String = AppConstant.API_KEY
    ): Response<NewsResponse>
}