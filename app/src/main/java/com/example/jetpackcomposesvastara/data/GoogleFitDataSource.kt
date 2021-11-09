package com.example.jetpackcomposesvastara.data

import android.content.Context
import android.util.Log
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.*
import com.google.android.gms.fitness.data.Goal.*
import com.google.android.gms.fitness.request.GoalsReadRequest
import com.google.android.gms.fitness.request.OnDataPointListener
import com.google.android.gms.fitness.request.SensorRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit
import kotlin.math.ceil

@ExperimentalComposeUiApi
class GoogleFitDataSource(@ApplicationContext private val mainActivity: Context)
{
    val FIT_TAG = "FIT_LOG"

    private fun getGoogleAccount(): GoogleSignInAccount = GoogleSignIn.getAccountForExtension(mainActivity, fitnessOption)

    private val stepsMutable: MutableLiveData<Int> = MutableLiveData()
    val stepsLiveData: LiveData<Int> = stepsMutable

    private val caloriesMutable: MutableLiveData<Int> = MutableLiveData()
    val caloriesLiveData: LiveData<Int> = caloriesMutable

    private val distanceMutable: MutableLiveData<Int> = MutableLiveData()
    val distanceLiveData: LiveData<Int> = distanceMutable

    private val goalsStepsMutable: MutableLiveData<Int> = MutableLiveData()
    val goalsStepsLiveData: LiveData<Int> = goalsStepsMutable

    fun getAsyncTodaySteps()
    {
        var total = -1
        Fitness.getHistoryClient(mainActivity, getGoogleAccount())
            .readDailyTotal(TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener { dataSet ->
                total = when {
                    dataSet.isEmpty -> 0
                    else -> formatValue(dataSet.dataPoints.first().getValue(Field.FIELD_STEPS))
                }
                stepsMutable.postValue(total)
            }
            .addOnFailureListener {
                stepsMutable.postValue(total)
            }
        getDataUsingSensor(TYPE_STEP_COUNT_DELTA, stepsListener)
    }

    private fun subscribeAndRecordData(dataType: DataType) {
        Fitness.getRecordingClient(mainActivity, getGoogleAccount())
            .subscribe(dataType)
            .addOnSuccessListener {
                Log.d(FIT_TAG,"RecordingClient $dataType")
            }
            .addOnFailureListener {
                Log.e(FIT_TAG, "RecordingClient failed. $dataType", it)
            }
    }

    private fun getDataUsingSensor(dataType: DataType, listener: OnDataPointListener) {
        Fitness.getSensorsClient(mainActivity, getGoogleAccount())
            .add(SensorRequest.Builder()
                    .setDataType(dataType) // Can't be omitted.
                    .setSamplingRate(10, TimeUnit.SECONDS)
                    .build(),
                listener
            )
            .addOnSuccessListener {
                Log.i(FIT_TAG, "Sensor registered! $dataType")
            }
            .addOnFailureListener {
                Log.e(FIT_TAG, "Listener not registered.  $dataType", it)
            }
    }

    private val stepsListener = OnDataPointListener { dataPoint ->
        for (field in dataPoint.dataType.fields) {
            val value = dataPoint.getValue(field)
            val valueCurr = stepsMutable.value
            Log.i(FIT_TAG, "DataPoint value: $value ; field: ${field.name}")
            if (valueCurr != null) {
                stepsMutable.postValue((valueCurr + value.toString().toInt()))
            }
        }
    }

    fun getAsyncTodayCalories()
    {
        var total = -1
        Fitness.getHistoryClient(mainActivity, getGoogleAccount())
            .readDailyTotal(TYPE_CALORIES_EXPENDED)
            .addOnSuccessListener { dataSet ->
                total = when {
                    dataSet.isEmpty -> 0
                    else -> formatValue(dataSet.dataPoints.first().getValue(Field.FIELD_CALORIES))
                }
                caloriesMutable.postValue(total)
            }
            .addOnFailureListener {
                caloriesMutable.postValue(total)
            }
        getDataUsingSensor(TYPE_CALORIES_EXPENDED, caloriesListener)
    }

    private val caloriesListener = OnDataPointListener { dataPoint ->
        for (field in dataPoint.dataType.fields) {
            val value = dataPoint.getValue(field)
            val valueCurr = caloriesMutable.value
            Log.i(FIT_TAG, "DataPoint value: $value ; field: ${field.name}")
            if (valueCurr != null) {
                caloriesMutable.postValue((valueCurr + value.toString().toInt()))
            }
        }
    }

    fun getAsyncTodayDistance()
    {
        var total = -1
        Fitness.getHistoryClient(mainActivity, getGoogleAccount())
            .readDailyTotal(TYPE_DISTANCE_DELTA)
            .addOnSuccessListener { dataSet ->
                total = when {
                    dataSet.isEmpty -> 0
                    else -> formatValue(dataSet.dataPoints.first().getValue(Field.FIELD_DISTANCE))
                }
                distanceMutable.postValue(total)
            }
            .addOnFailureListener {
                distanceMutable.postValue(total)
            }
        getDataUsingSensor(TYPE_DISTANCE_DELTA, distanceListener)
    }

    private val distanceListener = OnDataPointListener { dataPoint ->
        for (field in dataPoint.dataType.fields) {
            val value = dataPoint.getValue(field)
            val valueCurr = distanceMutable.value
            Log.i(FIT_TAG, "DataPoint value: $value ; field: ${field.name}")
            if (valueCurr != null) {
                distanceMutable.postValue((valueCurr + value.toString().toInt()))
            }
        }
    }

    private fun formatValue(value: Value): Int = try {
        Log.v("ValueBefore", "value = $value")
            value.asInt()
        } catch (e: IllegalStateException) {
            ceil(value.asFloat()).toInt()
        }

    private val goalsReadRequest = GoalsReadRequest.Builder()
        .addDataType(TYPE_STEP_COUNT_DELTA)
        .build()

    fun readStepsGoals() {
        Fitness.getGoalsClient(mainActivity, getGoogleAccount())
            .readCurrentGoals(goalsReadRequest)
            .addOnSuccessListener { goals ->
                // There should be at most one heart points goal currently.
                goals.firstOrNull()?.apply {
                    // What is the value of the goal
                    val goalValue = metricObjective.value.toInt()
                    goalsStepsMutable.postValue(goalValue)
                    Log.i(FIT_TAG, "Goal value: $goalValue")

                    // How is the goal measured?
                    Log.i(FIT_TAG, "Goal Objective: $objective")

                    // How often does the goal repeat?
                    Log.i(FIT_TAG, "Goal Recurrence: $recurrenceDetails")
                }
            }
    }

    private val Goal.objective: String
        get() = when (objectiveType) {
            OBJECTIVE_TYPE_DURATION ->
                "Duration (s): ${durationObjective.getDuration(TimeUnit.SECONDS)}"
            OBJECTIVE_TYPE_FREQUENCY ->
                "Frequency : ${frequencyObjective.frequency}"
            OBJECTIVE_TYPE_METRIC ->
                "Metric : ${metricObjective.dataTypeName} - ${metricObjective.value}"
            else -> "Unknown objective"
        }

    private val Goal.recurrenceDetails: String
        get() = recurrence?.let {
            val period = when (it.unit) {
                Recurrence.UNIT_DAY -> "days"
                Recurrence.UNIT_WEEK -> "weeks"
                Recurrence.UNIT_MONTH -> "months"
                else -> "Unknown"
            }
            "Every ${recurrence!!.count} $period"
        } ?: "Does not repeat"

}