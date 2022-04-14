package com.eyepax.newsapp.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eyepax.newsapp.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: FavoriteRepository
) : ViewModel() {


    fun getFavoriteArticles() = repository.getSavedNews()

}