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
        filterList.add(Filter(1, "Healthy", true))
        filterList.add(Filter(1, "Technology"))
        filterList.add(Filter(1, "Finance"))
        filterList.add(Filter(1, "Arts"))
        filterList.add(Filter(1, "Environment"))
        return filterList
    }

    suspend fun getFilterNews(filterType: String) =
        newsApi.getFilterNews(filter = filterType)

    suspend fun getFilterNews(searchQuery: String, sortBy: String, pageNumber: Int) =
        newsApi.getFilterNews(filter = searchQuery, sortBy = sortBy, pageNumber = pageNumber)

}