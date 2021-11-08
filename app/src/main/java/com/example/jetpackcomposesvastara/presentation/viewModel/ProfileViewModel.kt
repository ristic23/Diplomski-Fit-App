package com.example.jetpackcomposesvastara.presentation.viewModel

import android.app.DatePickerDialog
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import com.example.jetpackcomposesvastara.presentation.composable.util.Date
import java.util.Calendar.getInstance

class ProfileViewModel : ViewModel()
{

    private val _time = MutableLiveData(
        Date(
            getInstance().get(Calendar.DAY_OF_MONTH),
            getInstance().get(Calendar.MONTH) + 1,
            getInstance().get(Calendar.YEAR),
        ).toString())
    var time: LiveData<String> = _time

    fun selectDateTime(context: Context) {
        val currentDateTime = getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(context, { _, year, month, day ->
            updateDateTime(Date(day, month + 1, year).toString())
        }, startYear, startMonth, startDay).show()
    }

    private fun updateDateTime(dateTime: String) {
        _time.value = dateTime
    }
}