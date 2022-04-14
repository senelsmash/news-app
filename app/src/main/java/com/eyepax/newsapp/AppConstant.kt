package com.eyepax.newsapp

class AppConstant {
    companion object {
        const val API_KEY = "ae3766a89dbc4c1ebe00709e5615bd9f"
        const val BASE_URL = "https://newsapi.org"
        const val SEARCH_NEWS_TIME_DELAY = 500L
        const val USER_NOT_EXIST = 0
        const val EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        const val QUERY_PAGE_SIZE = 10
        const val QUERY_CATEGORY = "health"
        const val QUERY_SORT_BY = "popularity"
        const val USER_DATASTORE = "user_data"
    }
}