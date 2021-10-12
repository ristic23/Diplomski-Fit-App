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

    private fun getMonthNumber(month: String): Int {
        return when(month)
        {
            "February" -> 2
            "March" -> 3
            "April" -> 4
            "May" -> 5
            "June" -> 6
            "July" -> 7
            "August" -> 8
            "September" -> 9
            "October" -> 10
            "November" -> 11
            "December" -> 12
            else -> 1
        }
    }

    fun fromString(text: String?) {
        if(text == null)
        {
            day = 0
            month = 0
            year = 0
            return
        }
        val split = text.split(" ")
        if(split.size > 2) {
            day = split[0].toInt()
            month = getMonthNumber(split[1])
            year = split[2].toInt()
        }
    }
}

