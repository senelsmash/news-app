package com.eyepax.newsapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eyepax.newsapp.R
import com.eyepax.newsapp.model.Filter
import kotlinx.android.synthetic.main.item_filter.view.*


class FilterAdapter : RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    inner class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Filter>() {
        override fun areItemsTheSame(oldItem: Filter, newItem: Filter): Boolean {
            return oldItem.filterId == newItem.filterId
        }

        override fun areContentsTheSame(oldItem: Filter, newItem: Filter): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        return FilterViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_filter,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Filter) -> Unit)? = null

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val filter = differ.currentList[position]

        holder.itemView.apply {
            if(filter.isSelected) {
                filter.isSelected = false
                tvFilter.setTextColor(resources.getColor(R.color.white))
                tvFilter.background = resources.getDrawable(R.drawable.rounded_filter)
            } else {
                tvFilter.setTextColor(resources.getColor(R.color.text_black))
                tvFilter.background = resources.getDrawable(R.drawable.rounded_filter_white)
            }
            tvFilter.text = filter.categoryName
            setOnClickListener {
                onItemClickListener?.let {
                    filter.isSelected = true
                    notifyDataSetChanged()
                    tvFilter.setTextColor(resources.getColor(R.color.white))
                    tvFilter.background = resources.getDrawable(R.drawable.rounded_filter)
                    it(filter)
                }
            }
        }
    }

    fun setOnItemClickListener(listener: (Filter) -> Unit) {
        onItemClickListener = listener
    }
}