package com.eyepax.newsapp.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Helper {

    fun getTimeFromZTime(zTime: String): String {
        val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val output = SimpleDateFormat("dd/MM/yyyy")

        var d: Date? = null
        try {
            d = input.parse("2018-02-02T06:54:57.744Z")
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return output.format(d)
    }
}