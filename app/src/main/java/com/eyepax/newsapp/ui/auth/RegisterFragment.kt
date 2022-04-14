package com.eyepax.newsapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.eyepax.newsapp.AppConstant
import com.eyepax.newsapp.R
import com.eyepax.newsapp.UserPreferences
import com.eyepax.newsapp.model.User
import com.eyepax.newsapp.ui.AuthActivity
import com.eyepax.newsapp.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.btnRegister
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterFragment : Fragment(R.layout.fragment_register) {
    private val mViewModel by lazy {
        ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
    }
    lateinit var dataStoreManager: UserPreferences
    lateinit var mUser: User

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        clickEvents()
        dataStoreManager = UserPreferences(requireContext())

    }

    private fun clickEvents() {
        btnRegister.setOnClickListener {
            getUserDetailsAndRegister()
        }
    }

    private fun subscribeObservers() {
        mViewModel.registerUserId.observe(viewLifecycleOwner, Observer { userId ->
            if (userId != 0L) {
                mUser.id = userId?.toInt()
                lifecycleScope.launch(Dispatchers.IO) {
                    dataStoreManager.saveToDataStore(mUser)
                }.cancel()
                (activity as AuthActivity).startDashboardActivity()
                Log.d(TAG, "subscribeObservers: user insert id is: $userId")
            } else {
                Log.d(TAG, "subscribeObservers: user not inserted")
            }
        })
    }


    private fun getUserDetailsAndRegister() {
        val userName = etUserName.text?.trim().toString()
        val firstName = etFirstname.text?.trim().toString()
        val lastName = etLastname.text?.trim().toString()
        val password = etRetypePassword.text?.trim().toString()
        val passwordConfirmation = etPasswordReg.text?.trim().toString()
        val email = etEmail.text?.trim().toString()
        if (isValidInput(userName, firstName, lastName, password, passwordConfirmation, email)) {
            mUser = User(
                username = userName,
                firstName = firstName,
                lastName = lastName,
                password = password,
                emailAddress = email
            )
            mViewModel.registerUser(mUser)
        }
        Log.d(TAG, "getUserDetails: Validation error")
    }

    private fun isValidInput(
        username: String, firstName: String, lastName: String, password: String, passwordConfirmation: String, email: String
    ): Boolean {
        if (username.isNotEmpty() && firstName.isNotEmpty() && lastName.isNotEmpty()
            && password.isNotEmpty() && email.isNotEmpty() && passwordConfirmation.isNotEmpty()
        ) {
            if (email.matches(AppConstant.EMAIL_PATTERN.toRegex())) {
                return if (password == passwordConfirmation) {
                    true
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Password mismatch",
                        Toast.LENGTH_LONG
                    ).show()
                    false
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please enter valid email address",
                    Toast.LENGTH_LONG
                ).show()
                return false
            }

        } else {
            Toast.makeText(
                requireContext(),
                "Please enter all fields",
                Toast.LENGTH_LONG
            ).show()
            return false
        }
    }

    companion object {
        const val TAG = "RegisterFragment"

    }
}