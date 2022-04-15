package com.eyepax.newsapp.ui.search

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
class SearchViewModel @Inject constructor(
    private val repository: SharedRepository,
) : ViewModel() {

    val filterNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val filterList: MutableLiveData<MutableList<Filter>> = MutableLiveData()
    var filterNewsResponse: NewsResponse? = null
    var page = 1
    var isClearPreviousData = false

    fun getFilteredList(searchQuery: String, sortBy: String = "popularity", pageNumber: Int = 1) {
        viewModelScope.launch {
            safeFilterNews(searchQuery, sortBy, pageNumber)
        }
    }

    fun getFilterList() {
        val list = repository.getFilterList()
        filterList.postValue(list)
    }

    fun getTopHeadlines(countryCode: String, pageNumber: Int = 1) {
        viewModelScope.launch {
            safeTopHeadlinesCall(countryCode, pageNumber)
        }
    }

    private suspend fun safeTopHeadlinesCall(countryCode: String, pageNumber: Int) {
        filterNews.postValue(Resource.Loading())
        val response = repository.getTopHeadlines(countryCode)
        filterNews.postValue(requestFilterResponse(response))
    }

    private suspend fun safeFilterNews(searchQuery: String, sortBy: String, pageNumber: Int) {
        filterNews.postValue(Resource.Loading())
        val response = repository.getFilterNews(
            searchQuery = searchQuery, sortBy = sortBy, pageNumber = page
        )
        filterNews.postValue(requestFilterResponse(response))
    }


    private fun requestFilterResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { filteredResponse ->
                    page++
                    if (filterNewsResponse == null) {
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
        }  else {
            return Resource.Error(
                getErrorMessage(response.errorBody()?.string())
            )
        }
        return Resource.Error(response.message())
    }

    private fun getErrorMessage(response: String?): String {
        val jobj: JsonObject = Gson().fromJson(response, JsonObject::class.java)
        return jobj["message"].toString()
    }

}