package com.example.jetpackcomposesvastara.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcomposesvastara.data.RepositoryGoogleFitInterface
import com.example.repository.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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

    init {

        repositoryGoogleFitInterface.getAsyncTodaySteps()

        repositoryGoogleFitInterface.getTodayCalories()

        repositoryGoogleFitInterface.getTodayDistance()

//        repositoryGoogleFitInterface.readStepsGoals()

        CoroutineScope(Dispatchers.IO).launch {
            repositoryInterface.currentStepGoalFlow().collect {
                stepsGoalLiveData.postValue(it)
            }
        }

    }


}