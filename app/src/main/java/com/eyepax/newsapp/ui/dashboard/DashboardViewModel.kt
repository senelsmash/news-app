package com.eyepax.newsapp.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    var topHeadlinesResponse: NewsResponse? = null


    fun getTopHeadlines(countryCode: String) {
        viewModelScope.launch {
            safeTopHeadlinesCall(countryCode)
        }
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





}