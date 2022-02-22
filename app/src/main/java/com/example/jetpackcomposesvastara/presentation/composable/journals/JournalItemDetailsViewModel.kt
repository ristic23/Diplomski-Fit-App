package com.example.jetpackcomposesvastara.presentation.composable.journals

import android.app.TimePickerDialog
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.common.JournalDataObject
import com.example.jetpackcomposesvastara.util.Constants
import com.example.repository.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class JournalItemDetailsViewModel @Inject constructor(
    private val repository: RepositoryInterface
) : ViewModel()
{

    var journalDataObject = MutableLiveData(JournalDataObject())
        private set

    fun getSpecificJournalDataObject(uid: Int)
    {
        CoroutineScope(Dispatchers.IO).launch {
            journalDataObject.postValue(repository.readSpecificJournal(uid))
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


    fun selectTime(context: Context, timePicked: (String) -> Unit)
    {
        val currentDateTime = Calendar.getInstance(Locale.GERMANY)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)
        TimePickerDialog(
            context,
            {_, hour : Int, minute: Int ->
                timePicked(String.format(Locale.ROOT, "%02d:%02d",hour, minute))
            }
            , startHour, startMinute, true
        ).show()
    }


}