package com.example.jetpackcomposesvastara.data

import androidx.lifecycle.LiveData

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


}