package com.example.jetpackcomposesvastara.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.CalendarDayObject
import com.example.jetpackcomposesvastara.data.RepositoryGoogleFitInterface
import com.example.jetpackcomposesvastara.util.Constants
import com.example.repository.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repositoryGoogleFitInterface: RepositoryGoogleFitInterface,
    private val repositoryInterface: RepositoryInterface
): ViewModel()
{
    val stepsLiveData: LiveData<Int> = repositoryGoogleFitInterface.stepsLiveData

    val caloriesLiveData: LiveData<Int> = repositoryGoogleFitInterface.caloriesLiveData

    val distanceLiveData: LiveData<Int> = repositoryGoogleFitInterface.distanceLiveData

    val stepsGoalLiveData = MutableLiveData(0)

    val currStreakLiveData = MutableLiveData(0)
    val allTimeRecordLiveData = MutableLiveData(0)
    private var currentStreak = 0
    private var allTimeRecord = 0


    init {

        repositoryGoogleFitInterface.getAsyncTodaySteps()

        repositoryGoogleFitInterface.getTodayCalories()

        repositoryGoogleFitInterface.getTodayDistance()

        CoroutineScope(Dispatchers.IO).launch {
            repositoryInterface.currentStreakFlow().collect {
                currentStreak = it
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            repositoryInterface.allTimeRecordFlow().collect {
                allTimeRecord = it
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            repositoryInterface.currentStepGoalFlow().collect {
                stepsGoalLiveData.postValue(it)
                readLastDayUpdate()
            }
        }

    }

    private fun readLastDayUpdate()
    {
        CoroutineScope(Dispatchers.IO).launch {
            val lastUpdateMillis = repositoryInterface.currentLastTimeUpdateIsDoneFlow().first()

            if(lastUpdateMillis == -1L)
            {
                //First use calculate last 1 year
                val calendar = Calendar.getInstance(Locale.GERMANY)
                val endTime = calendar.timeInMillis
                calendar.add(Calendar.YEAR, -1)
                val startTime = calendar.timeInMillis
                repositoryGoogleFitInterface.getSpecificWeek(
                    startWeekTime = startTime,
                    endWeekTime = endTime,
                    readingDone = { list ->
                        val currentStepGoal = stepsGoalLiveData.value ?: 5000
                        var max = 0
                        var currentMax = 0

                        list.forEach { dayObject ->
                            if(dayObject.stepAchieved >= currentStepGoal)
                            {
                                currentMax++
                                if(currentMax > max)
                                    max++
                            }
                            else
                                currentMax = 0
                        }
                        currStreakLiveData.postValue(currentMax)
                        allTimeRecordLiveData.postValue(max)
                        CoroutineScope(Dispatchers.IO).launch {
                            repositoryInterface.setCurrStreak(currentMax)
                            repositoryInterface.setAllTimeRecord(max)
                        }
                    }
                )
            }
            else
            {
                //Check to see if update needed
                val calendar = Calendar.getInstance(Locale.GERMANY)
                val lastUpdateCalendar = Calendar.getInstance(Locale.GERMANY)
                lastUpdateCalendar.timeInMillis = lastUpdateMillis
                if(calendar.before(lastUpdateCalendar))
                {
                    repositoryGoogleFitInterface.getSpecificWeek(
                        startWeekTime = lastUpdateCalendar.timeInMillis,
                        endWeekTime = calendar.timeInMillis,
                        readingDone = { list ->
                            val currentStepGoal = stepsGoalLiveData.value ?: 5000
                            var max = 0
                            var currentMax = 0

                            list.forEach { dayObject ->
                                if(dayObject.stepAchieved >= currentStepGoal)
                                {
                                    currentMax++
                                    if(currentMax > max)
                                        max++
                                }
                                else
                                    currentMax = 0
                            }

                            currStreakLiveData.postValue(if(currentMax > currentStreak) currentMax else currentStreak)
                            allTimeRecordLiveData.postValue(if(max > allTimeRecord) max else allTimeRecord)

                            CoroutineScope(Dispatchers.IO).launch {
                                if (currentMax > currentStreak)
                                    repositoryInterface.setCurrStreak(currentMax)
                                if (max > allTimeRecord)
                                    repositoryInterface.setAllTimeRecord(max)
                            }
                        }
                    )
                }
                else
                {
                    currStreakLiveData.postValue(currentStreak)
                    allTimeRecordLiveData.postValue(allTimeRecord)
                }
            }

        }
        CoroutineScope(Dispatchers.IO).launch {
            delay(200)
            val calendar = Calendar.getInstance(Locale.GERMANY)
            calendar.add(Calendar.MINUTE, -5)
            repositoryInterface.setNewLastTimeUpdateIsDone(calendar.timeInMillis)
        }
    }


}