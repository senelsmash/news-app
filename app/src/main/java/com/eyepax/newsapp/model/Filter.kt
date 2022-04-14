package com.eyepax.newsapp.model

data class Filter(
    val filterId: Int,
    val categoryName: String,
    var isSelected: Boolean = false
)
