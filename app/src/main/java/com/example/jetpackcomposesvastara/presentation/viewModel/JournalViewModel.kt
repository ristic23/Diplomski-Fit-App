package com.example.jetpackcomposesvastara.presentation.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.common.JournalDataObject
import com.example.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel()
{
    var journalsList = mutableStateListOf<JournalDataObject>()
        private set

    init {
        CoroutineScope(Dispatchers.IO).launch {
            repository.getAllJournals().onEach {
                if(journalsList.isNotEmpty())
                    journalsList.clear()
                journalsList.addAll(it)
            }
        }
    }

}