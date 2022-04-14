package com.eyepax.newsapp

class AppConstant {
    companion object {
        const val API_KEY = "3c0a91a357924f73b0ec2e2c6c38f6a1"
        const val BASE_URL = "https://newsapi.org"
        const val SEARCH_NEWS_TIME_DELAY = 500L
        const val USER_NOT_EXIST = 0
        const val EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        const val QUERY_PAGE_SIZE = 20
        const val QUERY_CATEGORY = "health"
        const val QUERY_SORT_BY = "popularity"
        const val USER_DATASTORE = "user_data"
    }
}