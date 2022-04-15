package com.eyepax.newsapp.ui.auth

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
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {
    private val mViewModel by lazy {
        ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscriberObservers()
        redirectIfAlreadyLoggedIn()
        clickEvents(view)
    }

    private fun subscriberObservers() {
        mViewModel.userData.observe(
            viewLifecycleOwner, Observer {
                if(!it?.username.isNullOrEmpty()){
                    (activity as AuthActivity).startDashboardActivity()
                }
            }
        )
    }

    private fun redirectIfAlreadyLoggedIn() {
        mViewModel.getUser()
    }

    private fun clickEvents(view: View) {
        btnRegister.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.registerFragment)
        }

        btnLogin.setOnClickListener {
            if (etPassword.text.isEmpty() || etUsername.text.isEmpty()) {
                showMessage(
                    "Please enter username and password",
                )
                return@setOnClickListener
            }
            mViewModel.loginUser(
                etUsername.text.trim().toString(),
                etPassword.text.trim().toString()
            ).observe(viewLifecycleOwner, Observer { user ->
                if (user.isNotEmpty()) {
                    Log.d(TAG, "LOGIN: user exist")
                    mViewModel.setUser(user[0])
                    (activity as AuthActivity).startDashboardActivity()
                } else {
                    showMessage("Invalid credentials")
                    Log.d(TAG, "LOGIN: user doesn't exist")
                }
            })
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_LONG
        ).show()
    }


    companion object {
        const val TAG = "LoginFragment"
    }
}