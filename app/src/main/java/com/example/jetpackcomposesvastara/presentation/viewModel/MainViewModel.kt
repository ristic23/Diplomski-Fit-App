package com.example.jetpackcomposesvastara.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcomposesvastara.data.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repositoryInterface: RepositoryInterface
): ViewModel()
{
    val stepsLiveData: LiveData<Int> = repositoryInterface.stepsLiveData

    private val caloriesMutable: MutableLiveData<Int> = MutableLiveData()
    val caloriesLiveData: LiveData<Int> = caloriesMutable

    private val distanceMutable: MutableLiveData<Int> = MutableLiveData()
    val distanceLiveData: LiveData<Int> = distanceMutable

    init {
        repositoryInterface.getAsyncTodaySteps()

        repositoryInterface.getTodayCalories().onEach { stepsNumber ->
            caloriesMutable.postValue(stepsNumber)
        }
        repositoryInterface.getTodayDistance().onEach { distanceNumber ->
            distanceMutable.postValue(distanceNumber)
        }
    }



}