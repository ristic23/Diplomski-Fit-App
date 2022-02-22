package com.example.jetpackcomposesvastara.data

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.LiveData
import com.example.common.CalendarDayObject
import javax.inject.Inject

@ExperimentalComposeUiApi
class RepositoryGoogleFit @Inject constructor(private val googleFitDataSource: GoogleFitDataSource) : RepositoryGoogleFitInterface
{
    override val stepsLiveData: LiveData<Int> = googleFitDataSource.stepsLiveData

    override val caloriesLiveData: LiveData<Int> = googleFitDataSource.caloriesLiveData

    override val distanceLiveData: LiveData<Int> = googleFitDataSource.distanceLiveData

    override val goalsStepsLiveData: LiveData<Int> = googleFitDataSource.goalsStepsLiveData

    override fun getAsyncTodaySteps() = googleFitDataSource.getAsyncTodaySteps()

    override fun getTodayCalories() = googleFitDataSource.getAsyncTodayCalories()

    override fun getTodayDistance() = googleFitDataSource.getAsyncTodayDistance()

    override fun readStepsGoals() = googleFitDataSource.readStepsGoals()

    override fun getSpecificWeek(
        startWeekTime: Long,
        endWeekTime: Long,
        readingDone: (List<CalendarDayObject>) -> Unit
    ) {
        googleFitDataSource.getSpecificWeek(
            startWeekTime = startWeekTime,
            endWeekTime = endWeekTime,
            readingDone = {
                readingDone(it)
            }
        )
    }

}