package com.eyepax.newsapp.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.eyepax.newsapp.R
import com.eyepax.newsapp.ui.adapter.HeadlinesAdapter
import com.eyepax.newsapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dashboard.*

@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private lateinit var headlineAdapter: HeadlinesAdapter
    private val mViewModel by lazy {
        ViewModelProvider(requireActivity()).get(DashboardViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.getTopHeadlines("us")
        subscribeObservers()
        setupRecyclerView()
        headlineAdapter.setOnItemClickListener {

        }
    }

    private fun setupRecyclerView() {
        headlineAdapter = HeadlinesAdapter()
        rvTopHeadlines.apply {
            adapter = headlineAdapter
            layoutManager = LinearLayoutManager(
                activity, LinearLayoutManager.HORIZONTAL, false
            )
        }
    }

    private fun subscribeObservers() {
        mViewModel.topHeadlines.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { headlineResponse ->
                        Log.d(TAG, "subscribeObservers: ${headlineResponse.articles.size}")
                        headlineAdapter.differ.submitList(headlineResponse.articles.toList())
                    }
                }
                is Resource.Error -> {
//                    hideprogressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG)
                            .show()
//                        showErrorMessage(message)
                    }
                }
                is Resource.Loading -> {
//                    showProgressBar()
                }
            }

        })
    }

    companion object {
        const val TAG = "DashboardFragment"
    }
}