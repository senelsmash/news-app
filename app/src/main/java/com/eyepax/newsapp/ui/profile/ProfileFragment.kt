package com.eyepax.newsapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.eyepax.newsapp.R
import com.eyepax.newsapp.UserPreferences
import com.eyepax.newsapp.model.User
import com.eyepax.newsapp.ui.AuthActivity
import com.eyepax.newsapp.ui.MainActivity
import com.eyepax.newsapp.ui.auth.AuthViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val mViewModel by lazy {
        ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
    }
    private lateinit var user: User

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscriberObservers()
        mViewModel.getUser()

        btnLogout.setOnClickListener {
            if (it != null) {
                mViewModel.logoutUser(user)
                Log.d(TAG, "onViewCreated: User logout")
                startActivity(Intent(activity, AuthActivity::class.java))
                (activity as MainActivity).finish()
            }
        }
    }

    private fun subscriberObservers() {
        mViewModel.userData.observe(
            viewLifecycleOwner, Observer {
                if (it != null) {
                    user = it
                }

                tvFirstName.text = it?.firstName
                tvLastName.text = it?.lastName
                tvUsername.text = it?.username
                tvEmail.text = it?.emailAddress
            }
        )
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).showBottomNavigation(false)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).showBottomNavigation(true)
    }

    companion object {
        const val TAG = "ProfileFragment"
    }

}