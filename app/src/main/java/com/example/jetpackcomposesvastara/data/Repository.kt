package com.example.jetpackcomposesvastara.data

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalComposeUiApi
class Repository @Inject constructor(private val googleFitDataSource: GoogleFitDataSource) : RepositoryInterface
{
    override val stepsLiveData: LiveData<Int> = googleFitDataSource.stepsLiveData

    override fun getAsyncTodaySteps() = googleFitDataSource.getAsyncTodaySteps()

    override fun getTodayCalories(): Flow<Int> = googleFitDataSource.todayCalories

    override fun getTodayDistance(): Flow<Int> = googleFitDataSource.todayDistance

}