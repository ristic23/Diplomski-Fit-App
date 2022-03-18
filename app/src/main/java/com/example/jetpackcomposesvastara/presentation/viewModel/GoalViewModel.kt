package com.example.jetpackcomposesvastara.presentation.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.CalendarDayObject
import com.example.jetpackcomposesvastara.data.RepositoryGoogleFitInterface
import com.example.jetpackcomposesvastara.presentation.composable.journals.formatDate
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
class GoalViewModel @Inject constructor(
    private val repositoryGoogleFitInterface: RepositoryGoogleFitInterface,
    private val repository: RepositoryInterface
) : ViewModel()
{
    var stepsLiveData = MutableLiveData(0)
    var weekRangeLiveData = MutableLiveData("")
    private var endCalendar: Calendar = Calendar.getInstance(Locale.GERMANY)
    private var startCalendar: Calendar = Calendar.getInstance(Locale.GERMANY)

    var weekListData = MutableLiveData<MutableList<CalendarDayObject>>(mutableListOf())

    private val stepsGoalLiveData = MutableLiveData(0)

    val currStreakLiveData = MutableLiveData(0)
    val allTimeRecordLiveData = MutableLiveData(0)
    private var currentStreak = 0
    private var allTimeRecord = 0

    init {
        CoroutineScope(Dispatchers.IO).launch {
            repository.currentStepGoalFlow().collect {
                stepsLiveData.postValue(it)
            }
        }

        setEndDayTime(endCalendar)
        val initBackDays = when(endCalendar.get(Calendar.DAY_OF_WEEK))
        {
           1 -> 6
           else -> endCalendar.get(Calendar.DAY_OF_WEEK) - 2
        }

        setStartDayTime(startCalendar)
        startCalendar.add(Calendar.DAY_OF_MONTH, -initBackDays)
        getStepsForWeek()

        CoroutineScope(Dispatchers.IO).launch {
            repository.currentStreakFlow().collect {
                currentStreak = it
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            repository.allTimeRecordFlow().collect {
                allTimeRecord = it
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            repository.currentStepGoalFlow().collect {
                stepsGoalLiveData.postValue(it)
                readLastDayUpdate()
            }
        }
    }

    private val TAG = "LOG_GOAL_WEEK"
    private fun getStepsForWeek() {
        val endTime = endCalendar.timeInMillis
        val startTime = startCalendar.timeInMillis
        logWeek()
        repositoryGoogleFitInterface.getSpecificWeek(
            startWeekTime = startTime,
            endWeekTime = endTime,
            readingDone = {
                val tempList: MutableList<CalendarDayObject> = it as MutableList<CalendarDayObject>
                if(it.size < 7)
                {
                    val differenceSize = 7 - it.size
                    for(i in 0 until differenceSize)
                    {
                        tempList.add(CalendarDayObject())
                    }
                }
                else
                {
                    if(it.size > 7)
                    {
                        val differenceSize = it.size - 7
                        for(i in 0 until differenceSize)
                        {
                            tempList.removeAt(i)
                        }
                    }
                }
                weekListData.postValue(tempList)
            }
        )
    }

    fun nextWeek()
    {
        val tempTodayCalendar = Calendar.getInstance(Locale.GERMANY)
        endCalendar.add(Calendar.DAY_OF_MONTH, 1)
        if(
            endCalendar.time.before(tempTodayCalendar.time)
        ) {
            startCalendar.timeInMillis = endCalendar.timeInMillis
            endCalendar.add(Calendar.DAY_OF_MONTH, 6)
            setStartDayTime(startCalendar)
            setEndDayTime(endCalendar)
            getStepsForWeek()
        }
    }

    fun prevWeek()
    {
        startCalendar.add(Calendar.DAY_OF_MONTH, -1)
        endCalendar.timeInMillis = startCalendar.timeInMillis
        startCalendar.add(Calendar.DAY_OF_MONTH, -6)
        setStartDayTime(startCalendar)
        setEndDayTime(endCalendar)
        getStepsForWeek()
    }

    fun setNewStepGoal(newValue: Int) = viewModelScope.launch {
        repository.setNewStepDailyGoal(newValue)
    }

    private fun logWeek()
    {
        val tempStarCalendar = Calendar.getInstance(Locale.GERMANY)
        val tempEndCalendar = Calendar.getInstance(Locale.GERMANY)
        tempStarCalendar.timeInMillis = startCalendar.timeInMillis
        tempEndCalendar.timeInMillis = endCalendar.timeInMillis
        Log.v(TAG, "${formatDate(tempStarCalendar.time)} - ${formatDate(tempEndCalendar.time)}")
        weekRangeLiveData.postValue("${formatDate(tempStarCalendar.time)} - ${formatDate(tempEndCalendar.time)}")
    }

    private fun setEndDayTime(calendar: Calendar)
    {
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
    }

    private fun setStartDayTime(calendar: Calendar)
    {
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 1)
    }


    private fun readLastDayUpdate()
    {
        CoroutineScope(Dispatchers.IO).launch {
            val lastUpdateMillis = repository.currentLastTimeUpdateIsDoneFlow().first()

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
                            repository.setCurrStreak(currentMax)
                            repository.setAllTimeRecord(max)
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
                                    repository.setCurrStreak(currentMax)
                                if (max > allTimeRecord)
                                    repository.setAllTimeRecord(max)
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
            repository.setNewLastTimeUpdateIsDone(calendar.timeInMillis)
        }
    }

}