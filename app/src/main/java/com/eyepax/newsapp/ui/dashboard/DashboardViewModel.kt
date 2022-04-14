package com.eyepax.newsapp.ui.dashboard

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyepax.newsapp.model.Filter
import com.eyepax.newsapp.model.NewsResponse
import com.eyepax.newsapp.repository.SharedRepository
import com.eyepax.newsapp.utils.Resource
import com.google.gson.Gson
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: SharedRepository
) : ViewModel() {

    val topHeadlines: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val filterNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val filterList: MutableLiveData<MutableList<Filter>> = MutableLiveData()
    var page : Int = 0
    var topHeadlinesResponse: NewsResponse? = null
    var filterNewsResponse: NewsResponse? = null
    var isClearPreviousData = false

    fun getTopHeadlines(countryCode: String) {
        viewModelScope.launch {
            safeTopHeadlinesCall(countryCode)
        }
    }

    fun getNewsByCategory(category: String) {
        viewModelScope.launch {
//            page = 0
            safeCategoryNews(category, page)
        }
    }

    fun getNewsByCategoryNext(category: String) {
        viewModelScope.launch {
            page += 1
            safeCategoryNews(category, page)
        }
    }

    fun getFilterList() {
        val list = repository.getFilterList()
        filterList.postValue(list)
    }

    private fun requestTopHeadlinesResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { headlinesResponse ->
                topHeadlinesResponse = headlinesResponse
                return Resource.Success(topHeadlinesResponse ?: headlinesResponse)
            }
        } else {
            return Resource.Error(
                getErrorMessage(response.errorBody()?.string())
            )
        }
        return Resource.Error(message = response.message())
    }

    private fun requestCategoryNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { filteredResponse ->
                page++
                if(filterNewsResponse == null) {
                    filterNewsResponse = filteredResponse
                } else {
                    if(isClearPreviousData) {
                        filterNewsResponse?.articles?.clear()
                    }
                    val oldArticles = filterNewsResponse?.articles
                    val newArticles = filteredResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(filterNewsResponse ?: filteredResponse)
            }
        } else {
            return Resource.Error(
                getErrorMessage(response.errorBody()?.string())
            )
        }
        return Resource.Error(response.errorBody().toString())
    }

    private suspend fun safeTopHeadlinesCall(countryCode: String) {
        topHeadlines.postValue(Resource.Loading())
        val response = repository.getTopHeadlines(countryCode)
        topHeadlines.postValue(requestTopHeadlinesResponse(response))
    }

    private suspend fun safeCategoryNews(category: String, page: Int) {
        Log.d(DashboardViewModel::javaClass.name, "safeCategoryNews: cat: $category | Page: $page")
        filterNews.postValue(Resource.Loading())
        val response = repository.getNewsByCategory(category = category, page = page)
        filterNews.postValue(requestCategoryNewsResponse(response))
    }

    private fun getErrorMessage(response: String?): String {
        val jobj: JsonObject = Gson().fromJson(response, JsonObject::class.java)
        return jobj["message"].toString()
    }


}