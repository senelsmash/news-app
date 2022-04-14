package com.eyepax.newsapp

class AppConstant {
    companion object {
        const val API_KEY = "8c65dbcbcefd46b1928c42846043b170"
        const val BASE_URL = "https://newsapi.org"
        const val SEARCH_NEWS_TIME_DELAY = 500L
        const val USER_NOT_EXIST = 0
        const val EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        const val QUERY_PAGE_SIZE = 20
        const val USER_DATASTORE = "user_data"
    }
}