package com.eyepax.newsapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eyepax.newsapp.R
import com.eyepax.newsapp.model.Article
import com.eyepax.newsapp.utils.Helper
import kotlinx.android.synthetic.main.item_news_layout.view.*

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    companion object {
        private const val TYPE_NEWS = 0
        private const val TYPE_LOADING = 1
        private const val TYPE_COLLEAGUE = 2
        private const val TYPE_HEADER = 3
    }

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {

        val layout = when(viewType){
            TYPE_NEWS -> R.layout.item_news_layout
            TYPE_LOADING -> R.layout.item_loading_layout
            else -> throw IllegalArgumentException("Invalid view type")
        }

        val view = LayoutInflater
            .from(parent.context)
            .inflate(layout, parent, false)


        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_NEWS
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {

        val article = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(ivHeadlineImage)
            tvAuthor.text = if (article.author.isNullOrEmpty()) context.getString(R.string.n_a) else article.author
            tvHeadlineTitle.text = article.title
            tvDate.text = article.publishedAt?.let { Helper.getTimeFromZTime(it) }
            setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }
}