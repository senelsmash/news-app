package com.eyepax.newsapp.repository

import com.eyepax.newsapp.model.Filter
import com.eyepax.newsapp.network.NewsApiService
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val newsApi: NewsApiService
) {

    suspend fun getTopHeadlines(countryCode: String) =
        newsApi.getTopHeadlines(countryCode)

    fun getFilterList(): MutableList<Filter> {
        val filterList: MutableList<Filter> = emptyArray<Filter>().toMutableList()
        filterList.add(Filter(1, "Healthy"))
        filterList.add(Filter(1, "Technology"))
        filterList.add(Filter(1, "Finance"))
        filterList.add(Filter(1, "Arts"))
        filterList.add(Filter(1, "Environment"))
        return filterList
    }

    suspend fun getFilterNews(filterType: String) =
        newsApi.getFilterNews(filterType)

}