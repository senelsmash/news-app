package com.eyepax.newsapp.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eyepax.newsapp.R
import com.eyepax.newsapp.ui.adapter.NewsAdapter
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {
    private lateinit var newsAdapter: NewsAdapter
    private val mViewModel by lazy {
        ViewModelProvider(requireActivity()).get(FavoriteViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        mViewModel.getFavoriteArticles().observe(viewLifecycleOwner, Observer { articles ->
            newsAdapter.differ.submitList(articles)
        })

        clickEvents()
    }

    private fun clickEvents() {
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_navigation_favorite_to_newsDetailFragment,
                bundle
            )
        }

    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvNewsList.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    companion object {
        const val TAG = "FavoriteFragment"
    }
}