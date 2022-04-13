package com.eyepax.newsapp.model

data class Filter(
    val filterId: Int,
    val filterName: String,
    var isSelected: Boolean = false
)
