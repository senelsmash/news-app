package com.eyepax.newsapp.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.eyepax.newsapp.R
import com.eyepax.newsapp.UserPreferences
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    lateinit var dataStoreManager: UserPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataStoreManager = UserPreferences(requireContext())

        lifecycleScope.launch(Dispatchers.IO) {
            dataStoreManager.getFromDataStore().catch { e ->
                Log.d(TAG, "onViewCreated: datastore error " + e.message)
            }.collect {
                Log.d(TAG, "onViewCreated: datastore success " + it.username)
                withContext(Dispatchers.Main) {
                    tvFirstName.text = it.firstName
                    tvLastName.text = it.lastName
                    tvUsername.text = it.username
                    tvEmail.text = it.emailAddress
                }
            }
        }

    }

    companion object {
        const val TAG = "ProfileFragment"
    }

}