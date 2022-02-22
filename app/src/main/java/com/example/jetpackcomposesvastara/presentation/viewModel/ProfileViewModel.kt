package com.example.jetpackcomposesvastara.presentation.viewModel

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.Gender
import java.util.*
import com.example.jetpackcomposesvastara.presentation.composable.util.Date
import com.example.jetpackcomposesvastara.util.Constants
import com.example.repository.RepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: RepositoryInterface
) : ViewModel()
{

    var firstNameLiveData = MutableLiveData("")
    var lastNameLiveData = MutableLiveData("")
    var genderLiveData = MutableLiveData(Gender.Male)
    var birthdayLiveData = MutableLiveData(Date(
        Calendar.getInstance(Locale.GERMANY).get(Calendar.DAY_OF_MONTH),
        Calendar.getInstance(Locale.GERMANY).get(Calendar.MONTH) + 1,
        Calendar.getInstance(Locale.GERMANY).get(Calendar.YEAR),
    ).toString())
    var weightLiveData = MutableLiveData(0)
    var heightLiveData = MutableLiveData(0)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            repository.currentFirstNameFlow().collect {
                firstNameLiveData.postValue(it)
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            repository.currentLastNameFlow().collect {
                lastNameLiveData.postValue(it)
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            repository.currentGenderFlow().collect {
                genderLiveData.postValue(it)
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            repository.currentBirthdayFlow().collect {
                birthdayLiveData.postValue(it)
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            repository.currentWeightFlow().collect {
                weightLiveData.postValue(it)
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            repository.currentHeightFlow().collect {
                heightLiveData.postValue(it)
            }
        }
    }

    fun setNewFirstName(newValue: String) = viewModelScope.launch {
        Log.v("ProfileViewModel", "first name = $newValue")
        repository.setNewFirstName(newValue)
    }

    fun setNewLastName(newValue: String) = viewModelScope.launch {
        Log.v("ProfileViewModel", "last name = $newValue")
        repository.setNewLastName(newValue)
    }

    fun setNewWeight(newValue: Int) = viewModelScope.launch {
         Log.v("ProfileViewModel", "weight = $newValue")
        repository.setNewWeight(newValue)
    }

    fun setNewHeight(newValue: Int) = viewModelScope.launch {
         Log.v("ProfileViewModel", "height = $newValue")
        repository.setNewHeight(newValue)
    }

    fun setNewBirthday(newValue: String) = viewModelScope.launch {
         Log.v("ProfileViewModel", "birthday = $newValue")
        repository.setNewBirthday(newValue)
    }

    fun setNewGender(newValue: Gender) = viewModelScope.launch {
         Log.v("ProfileViewModel", "gender = $newValue")
        repository.setNewGender(newValue)
    }

    fun selectDateTime(context: Context,
                       newDateIsPicked: (String) -> Unit)
    {
        val currentDateTime = Calendar.getInstance(Locale.GERMANY)
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(context, { _, year, month, day ->
            newDateIsPicked.invoke(Date(day, month + 1, year).toString())
        }, startYear, startMonth, startDay).show()
    }

}