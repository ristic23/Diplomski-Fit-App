package com.example.jetpackcomposesvastara.data

import android.content.Context
import android.util.Log
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataSource
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.OnDataPointListener
import com.google.android.gms.fitness.request.SensorRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit

@ExperimentalComposeUiApi
class GoogleFitDataSource(@ApplicationContext private val mainActivity: Context)
{
    val FIT_TAG = "FIT_LOG"

    private fun getGoogleAccount(): GoogleSignInAccount = GoogleSignIn.getAccountForExtension(mainActivity, fitnessOption)

    private val stepsMutable: MutableLiveData<Int> = MutableLiveData()
    val stepsLiveData: LiveData<Int> = stepsMutable

    fun getAsyncTodaySteps()
    {
        var total = -1
        Fitness.getHistoryClient(mainActivity, getGoogleAccount())
            .readDailyTotal(TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener { dataSet ->
                total = when {
                    dataSet.isEmpty -> 0
                    else -> dataSet.dataPoints.first().getValue(Field.FIELD_STEPS).asInt()
                }
                stepsMutable.postValue(total)
            }
            .addOnFailureListener {
                stepsMutable.postValue(total)
            }
        getDataUsingSensor(TYPE_STEP_COUNT_DELTA)
    }

    private fun getDataUsingSensor(dataType: DataType) {
//        Fitness.getSensorsClient(mainActivity, getGoogleAccount())
//            .add(SensorRequest.Builder()
//                .setDataType(dataType)
//                .setSamplingRate(5, TimeUnit.SECONDS)
//                .build()) { dataPoint ->
//                when(dataType)
//                {
//                    TYPE_STEP_COUNT_DELTA -> {
//                        stepsMutable.postValue(dataPoint.getValue(Field.FIELD_STEPS).asInt())
//                    }
//                    TYPE_CALORIES_EXPENDED -> {
//                        //todo read real time calories
//                    }
//                    TYPE_DISTANCE_DELTA -> {
//                        //todo read real time distance
//                    }
//                }
//            }

        Fitness.getSensorsClient(mainActivity, getGoogleAccount())
            .add(SensorRequest.Builder()
                    .setDataType(dataType) // Can't be omitted.
                    .setSamplingRate(10, TimeUnit.SECONDS)
                    .build(),
                listener
            )
            .addOnSuccessListener {
                Log.i(FIT_TAG, "Listener registered!")
            }
            .addOnFailureListener {
                Log.e(FIT_TAG, "Listener not registered.", it)
            }
    }

    private val listener = OnDataPointListener { dataPoint ->
        for (field in dataPoint.dataType.fields) {
            val value = dataPoint.getValue(field)
            val valueCurr = stepsMutable.value
            Log.i(FIT_TAG, "Detected DataPoint field: ${field.name}")
            Log.i(FIT_TAG, "Detected DataPoint value: $value")
            if (valueCurr != null) {
                stepsMutable.postValue((valueCurr + value.toString().toInt()))
            }
        }
    }

    val todayCalories: Flow<Int> =
        flow {
            Fitness.getHistoryClient(mainActivity, getGoogleAccount())
                .readDailyTotal(TYPE_CALORIES_EXPENDED)
                .addOnSuccessListener { dataSet ->
                    val total = when {
                        dataSet.isEmpty -> 0
                        else -> dataSet.dataPoints.first().getValue(Field.FIELD_CALORIES).asInt()
                    }
                    flow {
                        emit(total)
                    }
                }
                .addOnFailureListener {
                    flow {
                        emit(0)
                    }
                }
        }

    val todayDistance: Flow<Int> =
        flow {
            Fitness.getHistoryClient(mainActivity, getGoogleAccount())
                .readDailyTotal(TYPE_DISTANCE_DELTA)
                .addOnSuccessListener { dataSet ->
                    val total = when {
                        dataSet.isEmpty -> 0
                        else -> dataSet.dataPoints.first().getValue(Field.FIELD_DISTANCE).asInt()
                    }
                    flow {
                        emit(total)
                    }
                }
                .addOnFailureListener {
                    flow {
                        emit(0)
                    }
                }
        }

}