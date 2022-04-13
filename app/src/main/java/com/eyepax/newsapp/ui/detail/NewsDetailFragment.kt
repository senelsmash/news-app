package com.eyepax.newsapp.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.eyepax.newsapp.R
import com.eyepax.newsapp.model.Article
import kotlinx.android.synthetic.main.fragment_news_detail.*
import kotlinx.android.synthetic.main.item_headline_layout.view.*


class NewsDetailFragment : Fragment(R.layout.fragment_news_detail) {
    val args: NewsDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = args.article
        Log.d(TAG, "onViewCreated: ${article.title}")
        initializeView(article)
    }

    private fun initializeView(article: Article) {
        Glide.with(this).load(article.urlToImage).into(ivNewsImage)
        tvDates.text = article.publishedAt
        tvNewsTitle.text = article.title
        tvContent.text = article.content + "\n" +article.description  //full content is not provided in api
        tvPublishedBy.text = article.author

    }

    companion object {
        const val TAG = "NewsDetailFragment"
    }
}