package com.eyepax.newsapp.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eyepax.newsapp.R
import com.eyepax.newsapp.ui.adapter.FilterAdapter
import com.eyepax.newsapp.ui.adapter.HeadlinesAdapter
import com.eyepax.newsapp.ui.adapter.NewsAdapter
import com.eyepax.newsapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dashboard.*

@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private lateinit var headlineAdapter: HeadlinesAdapter
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var filterAdapter: FilterAdapter
    private val mViewModel by lazy {
        ViewModelProvider(requireActivity()).get(DashboardViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.getTopHeadlines("us")
        mViewModel.getFilterList()
        mViewModel.getFilteredList("Healthy")
        setupRecyclerView()
        subscribeObservers()
        headlineAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_navigation_dashboard_to_newsDetailFragment,
                bundle
            )
        }

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_navigation_dashboard_to_newsDetailFragment,
                bundle
            )
        }

        filterAdapter.setOnItemClickListener {
            mViewModel.getFilteredList(it.filterName)
        }

        etSearchView.isFocusable = false
        searchContainer.setOnClickListener {
            findNavController().navigate(
                R.id.action_navigation_dashboard_to_searchFragment
            )
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

        newsAdapter = NewsAdapter()
        rvNewsList.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        filterAdapter = FilterAdapter()
        rvFilter.apply {
            adapter = filterAdapter
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
                        Log.d(
                            TAG,
                            "subscribeObservers: Headline news ${headlineResponse.articles.size}"
                        )
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

        mViewModel.filterNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { filterNews ->
                        Log.d(TAG, "subscribeObservers Filtered News: ${filterNews.articles.size}")
                        newsAdapter.differ.submitList(filterNews.articles.toList())
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

        mViewModel.filterList.observe(viewLifecycleOwner, Observer { response ->
            filterAdapter.differ.submitList(response.toList())
        })
    }

    companion object {
        const val TAG = "DashboardFragment"
    }
}