package com.example.jetpackcomposesvastara.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.jetpackcomposesvastara.data.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repositoryInterface: RepositoryInterface
): ViewModel()
{
    val stepsLiveData: LiveData<Int> = repositoryInterface.stepsLiveData

    val caloriesLiveData: LiveData<Int> = repositoryInterface.caloriesLiveData

    val distanceLiveData: LiveData<Int> = repositoryInterface.distanceLiveData

    val stepsGoalLiveData: LiveData<Int> = repositoryInterface.goalsStepsLiveData

    init {

        repositoryInterface.getAsyncTodaySteps()

        repositoryInterface.getTodayCalories()

        repositoryInterface.getTodayDistance()

        repositoryInterface.readStepsGoals()

    }



}