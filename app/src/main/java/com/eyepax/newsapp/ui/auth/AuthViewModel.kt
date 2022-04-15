package com.eyepax.newsapp.ui.auth

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.eyepax.newsapp.UserPreferences
import com.eyepax.newsapp.model.User
import com.eyepax.newsapp.repository.AuthRepository
import com.eyepax.newsapp.repository.UserPreferenceRepository
import com.eyepax.newsapp.ui.AuthActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val preferences: UserPreferenceRepository
) : ViewModel() {

    val registerUserId: MutableLiveData<Long?> = MutableLiveData()
    val userData: MutableLiveData<User?> = MutableLiveData()

    fun registerUser(user: User) {
        viewModelScope.launch {
            val id = repository.insert(user)
            registerUserId.postValue(id)
        }
    }

    fun loginUser(username: String, password: String) = repository.getUser(username, password)

    fun getUser() {
        Log.d(AuthViewModel::javaClass.name, "getUser")

        viewModelScope.launch {
            preferences.getFromDataStore().catch { e ->
                Log.d(LoginFragment.TAG, "onViewCreated: datastore error " + e.message)
            }.collect {
                Log.d(LoginFragment.TAG, "onViewCreated: datastore success " + it.username)
                userData.postValue(it)
            }
        }
    }

    fun setUser(user: User) {
        Log.d(AuthViewModel::javaClass.name, "setUser: $user")
        viewModelScope.launch {
            preferences.saveToDataStore(user)
        }
    }

    fun logoutUser(user: User) {
        Log.d(AuthViewModel::javaClass.name, "setUser: $user")
        viewModelScope.launch {
            preferences.clearDataStore(user)
        }
    }
}