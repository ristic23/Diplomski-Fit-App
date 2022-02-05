package com.example.jetpackcomposesvastara.presentation.composable.journals

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.common.JournalDataObject
import com.example.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class JournalItemDetailsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel()
{

    var journalDataObject = MutableLiveData(JournalDataObject())
        private set

    fun getSpecificJournalDataObject(uid: Int)
    {
        CoroutineScope(Dispatchers.IO).launch {
            journalDataObject.value = repository.readSpecificJournal(uid)
        }
    }

    fun saveOrUpdateJournalDataObject(newObject: JournalDataObject, operationFinished: () -> Unit)
    {
        CoroutineScope(Dispatchers.IO).launch {
            newObject.let { journalData ->
                if(journalData.uid != -1)
                    repository.updateJournal(journalData)
                else
                    repository.saveJournal(journalData)
                withContext(Dispatchers.Main)
                {
                    operationFinished.invoke()
                }
            }
        }
    }

    fun deleteJournalDataObject(newObject: JournalDataObject, operationFinished: () -> Unit)
    {
        CoroutineScope(Dispatchers.IO).launch {
            newObject.let { journalData ->
                repository.deleteJournal(journalData)
                withContext(Dispatchers.Main)
                {
                    operationFinished.invoke()
                }
            }
        }
    }



}