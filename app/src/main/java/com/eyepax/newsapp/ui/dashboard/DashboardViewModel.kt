package com.eyepax.newsapp.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyepax.newsapp.model.Filter
import com.eyepax.newsapp.model.NewsResponse
import com.eyepax.newsapp.utils.Resource
import com.eyepax.newsapp.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: DashboardRepository
) : ViewModel() {

    val topHeadlines: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val filterNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val filterList: MutableLiveData<MutableList<Filter>> = MutableLiveData()
    var topHeadlinesResponse: NewsResponse? = null
    var filterNewsResponse: NewsResponse? = null


    fun getTopHeadlines(countryCode: String) {
        viewModelScope.launch {
            safeTopHeadlinesCall(countryCode)
        }
    }

    fun getFilteredList(filterType: String) {
        viewModelScope.launch {
            safeFilterNews(filterType)
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
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeTopHeadlinesCall(countryCode: String) {
        topHeadlines.postValue(Resource.Loading())
        val response = repository.getTopHeadlines(countryCode)
        topHeadlines.postValue(requestTopHeadlinesResponse(response))
    }

    private suspend fun safeFilterNews(filterType: String) {
        filterNews.postValue(Resource.Loading())
        val response = repository.getFilterNews(filterType)
        filterNews.postValue(requestFilterResponse(response))
    }

    private fun requestFilterResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { filteredResponse ->
                filterNewsResponse = filteredResponse
                return Resource.Success(filteredResponse)
            }
        }
        return Resource.Error(response.message())
    }


}