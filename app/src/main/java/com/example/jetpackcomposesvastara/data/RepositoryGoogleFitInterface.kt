package com.example.jetpackcomposesvastara.data

import androidx.lifecycle.LiveData
import com.example.common.CalendarDayObject

interface RepositoryGoogleFitInterface
{
    val stepsLiveData: LiveData<Int>

    val distanceLiveData: LiveData<Int>

    val caloriesLiveData: LiveData<Int>

    val goalsStepsLiveData: LiveData<Int>

    fun getAsyncTodaySteps()

    fun getTodayCalories()

    fun getTodayDistance()

    fun readStepsGoals()

    fun getSpecificWeek(startWeekTime: Long, endWeekTime: Long, readingDone: (List<CalendarDayObject>) -> Unit)

}