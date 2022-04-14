package com.eyepax.newsapp.repository

import com.eyepax.newsapp.model.Filter
import com.eyepax.newsapp.network.NewsApiService
import javax.inject.Inject

class SharedRepository @Inject constructor(
    private val newsApi: NewsApiService
) {

    suspend fun getTopHeadlines(countryCode: String) =
        newsApi.getTopHeadlines(countryCode)

    fun getFilterList(): MutableList<Filter> {
        val filterList: MutableList<Filter> = emptyArray<Filter>().toMutableList()
        filterList.add(Filter(1, "health", true))
        filterList.add(Filter(1, "entertainment"))
        filterList.add(Filter(1, "general"))
        filterList.add(Filter(1, "business"))
        filterList.add(Filter(1, "science"))
        filterList.add(Filter(1, "sports"))
        filterList.add(Filter(1, "technology"))
        return filterList
    }

    suspend fun getFilterNews(filterType: String) =
        newsApi.getFilterNews(filter = filterType)

    suspend fun getNewsByCategory(category: String, page: Int = 1) =
        newsApi.getByCategory(category = category, pageNumber = page)

    suspend fun getFilterNews(searchQuery: String, sortBy: String, pageNumber: Int) =
        newsApi.getFilterNews(filter = searchQuery, sortBy = sortBy, pageNumber = pageNumber)

}