package com.eyepax.newsapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyepax.newsapp.model.Article
import com.eyepax.newsapp.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    fun saveNewsArticle(article: Article) = viewModelScope.launch {
        favoriteRepository.insert(article)
    }
}