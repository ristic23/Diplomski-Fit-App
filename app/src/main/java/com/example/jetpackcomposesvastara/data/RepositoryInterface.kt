package com.example.jetpackcomposesvastara.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface
{
    val stepsLiveData: LiveData<Int>

    fun getAsyncTodaySteps()

    fun getTodayCalories(): Flow<Int>

    fun getTodayDistance(): Flow<Int>


}