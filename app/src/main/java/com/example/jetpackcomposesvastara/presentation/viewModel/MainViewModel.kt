package com.example.jetpackcomposesvastara.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.jetpackcomposesvastara.data.RepositoryGoogleFitInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repositoryGoogleFitInterface: RepositoryGoogleFitInterface
): ViewModel()
{
    val stepsLiveData: LiveData<Int> = repositoryGoogleFitInterface.stepsLiveData

    val caloriesLiveData: LiveData<Int> = repositoryGoogleFitInterface.caloriesLiveData

    val distanceLiveData: LiveData<Int> = repositoryGoogleFitInterface.distanceLiveData

    val stepsGoalLiveData: LiveData<Int> = repositoryGoogleFitInterface.goalsStepsLiveData

    init {

        repositoryGoogleFitInterface.getAsyncTodaySteps()

        repositoryGoogleFitInterface.getTodayCalories()

        repositoryGoogleFitInterface.getTodayDistance()

        repositoryGoogleFitInterface.readStepsGoals()

    }



}