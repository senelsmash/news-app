package com.eyepax.newsapp.repository

import com.eyepax.newsapp.AppConstant.Companion.QUERY_PAGE_SIZE
import com.eyepax.newsapp.model.Filter
import com.eyepax.newsapp.network.NewsApiService
import javax.inject.Inject

class SharedRepository @Inject constructor(
    private val newsApi: NewsApiService
) {

    suspend fun getTopHeadlines(countryCode: String, page: Int = 1) =
        newsApi.getTopHeadlines(countryCode = countryCode, pageSize = QUERY_PAGE_SIZE, pageNumber = page)

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

    suspend fun getFilterNews(filterType: String, page: Int) =
        newsApi.getFilterNews(filter = filterType, pageSize = QUERY_PAGE_SIZE, page = page)

    suspend fun getNewsByCategory(category: String, page: Int = 1) =
        newsApi.getByCategory(category = category, pageNumber = page, pageSize = QUERY_PAGE_SIZE)

    suspend fun getFilterNews(searchQuery: String, sortBy: String, pageNumber: Int) =
        newsApi.getFilterNews(filter = searchQuery, sortBy = sortBy, pageSize = QUERY_PAGE_SIZE, page = pageNumber)

}