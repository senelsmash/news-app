package com.eyepax.newsapp.ui.dashboard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eyepax.newsapp.AppConstant
import com.eyepax.newsapp.R
import com.eyepax.newsapp.ui.MainActivity
import com.eyepax.newsapp.ui.adapter.FilterAdapter
import com.eyepax.newsapp.ui.adapter.HeadlinesAdapter
import com.eyepax.newsapp.ui.adapter.NewsAdapter
import com.eyepax.newsapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dashboard.*

@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    var onScrollListener: ((status: Boolean) -> Unit)? = null

    private lateinit var headlineAdapter: HeadlinesAdapter
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var filterAdapter: FilterAdapter
    private var mSelectedCategory = ""
    private var isApiRequest = false
    private val mViewModel by lazy {
        ViewModelProvider(requireActivity())[DashboardViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.getTopHeadlines("us")
        mViewModel.getFilterList()
        mViewModel.getNewsByCategory("health")
        mSelectedCategory = "health"
        setupRecyclerView()
        subscribeObservers()
        clickEvents()
    }

    private fun clickEvents() {
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
            mViewModel.isClearPreviousData = true
            mViewModel.getNewsByCategory(it.categoryName)
            mSelectedCategory = it.categoryName
        }

        searchViewWidget.isFocusable = false
        searchViewContainer.setOnClickListener {
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
            hasFixedSize()
            addOnScrollListener(scrollListener)
        }

        implementedScrollListener()

        filterAdapter = FilterAdapter()
        rvFilter.apply {
            adapter = filterAdapter
            layoutManager = LinearLayoutManager(
                activity, LinearLayoutManager.HORIZONTAL, false
            )
        }

    }

    private fun implementedScrollListener() {
        nestedScrollView.setOnScrollChangeListener(mScrollChangeListener)
    }

    private val mScrollChangeListener =
        NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
                //Scrolling down
            }
            if (scrollY < oldScrollY) {
                //Scrolling up
            }

            if (scrollY == 0) {
                //Reaches to the top
            }

            if (v != null) {
                if (scrollY + v.measuredHeight == v.getChildAt(0).measuredHeight) {

                    if (!isApiRequest){

                        Log.d(TAG, "setOnScrollChangeListener: $scrollY")

                        isApiRequest = true
                        mViewModel.getNewsByCategoryNext(filterAdapter.selectedFilter)
                    }
                }
            }
        }

    private fun subscribeObservers() {
        mViewModel.topHeadlines.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    (activity as MainActivity).showLoading(false)
                    response.data?.let { headlineResponse ->
                        Log.d(
                            TAG,
                            "subscribeObservers: Headline news ${headlineResponse.articles.size}"
                        )
                        headlineAdapter.differ.submitList(headlineResponse.articles.toList())
                    }
                }
                is Resource.Error -> {
                    (activity as MainActivity).showLoading(false)
                    response.message?.let { message ->
                        showErrorMessage(message)
                    }
                }
                is Resource.Loading -> {
                    (activity as MainActivity).showLoading(true)
                }
            }

        })

        mViewModel.filterNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideErrorMessage()
                    (activity as MainActivity).showLoading(false)
                    response.data?.let { filterNews ->
                        Log.d(TAG, "subscribeObservers Filtered News: ${filterNews.articles.size}")
                        newsAdapter.differ.submitList(filterNews.articles.toList())

                        val r = Runnable {
                            isApiRequest = false
                        }
                        Handler(Looper.getMainLooper()).postDelayed(r, 2000)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    (activity as MainActivity).showLoading(false)
                    response.message?.let { message ->

                        showErrorMessage(message)

                        val r = Runnable {
                            isApiRequest = false
                        }
                        Handler(Looper.getMainLooper()).postDelayed(r, 2000)
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
                mViewModel.getNewsByCategory(mSelectedCategory)
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
                (activity as MainActivity).showBottomNavigation(false)
            } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                (activity as MainActivity).showBottomNavigation(true)

            }
        }
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).showBottomNavigation(false)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).showBottomNavigation(true)
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG)
            .show()
        isError = true
    }

    private fun hideErrorMessage() {
        isError = false
    }

    private fun hideProgressBar() {
        isLoading = false
    }

    private fun showProgressBar() {
        (activity as MainActivity).showLoading(true)
        isLoading = true
    }

    companion object {
        const val TAG = "DashboardFragment"
    }
}