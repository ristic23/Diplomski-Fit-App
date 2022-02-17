package com.example.jetpackcomposesvastara.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repository.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor(
    private val repository: RepositoryInterface
) : ViewModel()
{
    var stepsLiveData = MutableLiveData(0)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            repository.currentStepGoalFlow().collect {
                stepsLiveData.postValue(it)
            }
        }
    }

    fun setNewStepGoal(newValue: Int) = viewModelScope.launch {
        repository.setNewStepDailyGoal(newValue)
    }

}