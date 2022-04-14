package com.eyepax.newsapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.eyepax.newsapp.R
import com.eyepax.newsapp.UserPreferences
import com.eyepax.newsapp.ui.AuthActivity
import com.eyepax.newsapp.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {
    private val mViewModel by lazy {
        ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
    }
    lateinit var dataStoreManager: UserPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataStoreManager = UserPreferences(requireContext())
        clickEvents(view)
    }

    private fun clickEvents(view: View) {
        btnRegister.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.registerFragment)
        }

        btnLogin.setOnClickListener {
            if (etPassword.text.isEmpty() || etUsername.text.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please enter username and password",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            mViewModel.loginUser(
                etUsername.text.trim().toString(),
                etPassword.text.trim().toString()
            ).observe(viewLifecycleOwner, Observer { user ->
                if (user.isNotEmpty()) {
                    Log.d(TAG, "LOGIN: user exist")
                    lifecycleScope.launch(Dispatchers.IO) {
                        dataStoreManager.saveToDataStore(user[0])
                    }.cancel()
                    (activity as AuthActivity).startDashboardActivity()
                } else {
                    Log.d(TAG, "LOGIN: user doesn't exist")
                }
            })
        }
    }


    companion object {
        const val TAG = "LoginFragment"
    }
}