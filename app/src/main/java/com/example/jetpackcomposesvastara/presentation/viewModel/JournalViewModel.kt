package com.example.jetpackcomposesvastara.presentation.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.common.JournalDataObject
import com.example.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel()
{
    private var journalsList: List<JournalDataObject> = listOf()

    var journalsListFiltered = MutableLiveData<List<JournalDataObject>>(listOf())
        private set

    var isHydrationOn = mutableStateOf(true)
    var isWorkoutOn = mutableStateOf(true)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            repository.getAllJournals().collect { list ->
                journalsList = list
                journalsListFiltered.postValue(list)
            }
        }
    }

    fun filterUpdate(hydrationState: Boolean, workoutState: Boolean)
    {
        isHydrationOn.value = hydrationState
        isWorkoutOn.value = workoutState
        if(!isHydrationOn.value && !isWorkoutOn.value)
        {
            journalsListFiltered.postValue(listOf())
            return
        }
        if(isHydrationOn.value && isWorkoutOn.value)
        {
            journalsListFiltered.postValue(journalsList)
            return
        }

        var newList = journalsList.filter {
            if(isHydrationOn.value) it.isHydration else !it.isHydration
        }
        newList = newList.filter {
            if(isWorkoutOn.value) !it.isHydration else it.isHydration
        }
        journalsListFiltered.postValue(newList)
    }


}