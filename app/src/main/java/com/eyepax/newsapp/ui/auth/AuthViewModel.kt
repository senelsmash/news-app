package com.eyepax.newsapp.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyepax.newsapp.model.User
import com.eyepax.newsapp.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    val registerUserId: MutableLiveData<Long?> = MutableLiveData()

    fun registerUser(user: User) {
        viewModelScope.launch {
            val id = repository.insert(user)
            registerUserId.postValue(id)
        }
    }

    fun loginUser(username: String, password: String) = repository.getUser(username, password)


}