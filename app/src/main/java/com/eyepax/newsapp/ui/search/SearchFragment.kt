package com.eyepax.newsapp.ui.search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eyepax.newsapp.AppConstant
import com.eyepax.newsapp.AppConstant.Companion.QUERY_PAGE_SIZE
import com.eyepax.newsapp.AppConstant.Companion.SEARCH_NEWS_TIME_DELAY
import com.eyepax.newsapp.R
import com.eyepax.newsapp.ui.adapter.FilterAdapter
import com.eyepax.newsapp.ui.adapter.NewsAdapter
import com.eyepax.newsapp.ui.dashboard.DashboardFragment
import com.eyepax.newsapp.utils.Resource
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.etSearchView
import kotlinx.android.synthetic.main.fragment_dashboard.rvFilter
import kotlinx.android.synthetic.main.fragment_dashboard.rvNewsList
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var filterAdapter: FilterAdapter
    private var searchJob: Job? = null
    private var mSelectedFilter = ""
    private var mSearchedText = ""
    private val mViewModel by lazy {
        ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.getFilterList()
        mViewModel.getFilteredList("Healthy")
        mSelectedFilter = "Healthy"
        mSearchedText = mSelectedFilter
        setupRecyclerView()
        subscriberObservers()
        clickEvents()
        searchListener()
    }

    private fun showErrorMessage(message: String) {
        isError = true
    }

    private fun hideErrorMessage() {
        isError = false
    }

    private fun hideProgressBar() {
        isLoading = false
    }

    private fun showProgressBar() {
        isLoading = true
    }

    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= AppConstant.QUERY_PAGE_SIZE
            val shouldPaginate =
                isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                        isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                mViewModel.getFilteredList(mSearchedText)
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun searchListener() {
        etSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    performSearchJob(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                performSearchJob(newText)
                return true
            }
        })
    }

    private fun clickEvents() {
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchFragment_to_newsDetailFragment,
                bundle
            )
        }

        filterAdapter.setOnItemClickListener {
            mViewModel.getFilteredList(it.filterName)
            mSelectedFilter = it.filterName
        }
    }

    private fun performSearchJob(query: String) {
        searchJob?.cancel()
        searchJob = MainScope().launch {
            delay(SEARCH_NEWS_TIME_DELAY)
            if (query.isNotEmpty()) {
                mViewModel.getFilteredList(query)
                mSearchedText = query
            } else {
                mViewModel.getFilteredList(mSelectedFilter)
                mSearchedText = mSelectedFilter
            }

        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvNewsList.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(scrollListener)
        }

        filterAdapter = FilterAdapter()
        rvFilter.apply {
            adapter = filterAdapter
            layoutManager = LinearLayoutManager(
                activity, LinearLayoutManager.HORIZONTAL, false
            )
        }
    }

    private fun subscriberObservers() {
        mViewModel.filterNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { filterNews ->
                        Log.d(
                            DashboardFragment.TAG,
                            "subscribeObservers Filtered News: ${filterNews.articles.size}"
                        )
                        newsAdapter.differ.submitList(filterNews.articles.toList())
                        val totalPages = filterNews.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = mViewModel.page == totalPages
                        tvSearchCount.text = "About ${filterNews.totalResults} results for "
                        tvSearchText.text = mSearchedText

                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG)
                            .show()
                        showErrorMessage(message)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        mViewModel.filterList.observe(viewLifecycleOwner, Observer { response ->
            filterAdapter.differ.submitList(response.toList())
        })
    }

    companion object {
        const val TAG = "SearchFragment"
    }
}