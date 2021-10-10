package com.example.jetpackcomposesvastara.presentation.composable.util

import java.util.*

data class Date(
    var day:Int = 10,
    var month:Int = 10,
    var year:Int = 2021
)
{
    override fun toString(): String {
        return String.format(Locale.ROOT, "%02d %s %d", day, getMonthName(month), year)
    }

    private fun getMonthName(month: Int): String {
        return when(month)
        {
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11-> "November"
            12 -> "December"
            else -> "January"
        }
    }
}

