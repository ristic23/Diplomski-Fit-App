package com.example.jetpackcomposesvastara.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface
{
    val stepsLiveData: LiveData<Int>

    val distanceLiveData: LiveData<Int>

    val caloriesLiveData: LiveData<Int>

    val goalsStepsLiveData: LiveData<Int>

    fun getAsyncTodaySteps()

    fun getTodayCalories()

    fun getTodayDistance()

    fun readStepsGoals()


}