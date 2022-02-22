package com.example.jetpackcomposesvastara.googleFItTemp

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.jetpackcomposesvastara.data.*
import com.example.jetpackcomposesvastara.databinding.FragmentNotificationsBinding
import com.example.jetpackcomposesvastara.util.Constants
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.request.OnDataPointListener
import com.google.android.gms.fitness.request.SensorRequest
import com.google.android.gms.fitness.result.DataReadResponse
import com.google.android.gms.tasks.OnSuccessListener
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit

class GoogleFitFragment : Fragment(), OnSuccessListener<Any>
{
//    TYPE_STEP_COUNT_DELTA , TYPE_STEP_COUNT_CUMULATIVE


    private val GOOGLE_REQUESTE_CODE = 5563

    //todo @Inject with hilt
//    private val notificationsViewModel: NotificationsViewModel by viewModels()
//    {
//        GoogleFitViewModelFactory((requireContext().applicationContext as ApplicationClass).repository)
//    }

    private var googleFitBinding: FragmentNotificationsBinding? = null
    private val viewBinding get() = googleFitBinding!!

    private lateinit var mActivity: AppCompatActivity
    private lateinit var mContext: Context

//    private lateinit var fitnessOption: FitnessOptions

    private var fitnessDataResponseModel = FitnessDataResponseModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(activity is AppCompatActivity)
            mActivity = activity as AppCompatActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        googleFitBinding = FragmentNotificationsBinding.inflate(inflater, container, false)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkGoogleFitPermissions()

        viewBinding.getHistory.setOnClickListener {
            requestForHistory()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private fun checkGoogleFitPermissions()
    {

        val googleSignInAccount: GoogleSignInAccount = getGoogleAccount()

        if(!GoogleSignIn.hasPermissions(googleSignInAccount, fitnessOption))
        {
            GoogleSignIn.requestPermissions(
                mActivity,
                GOOGLE_REQUESTE_CODE,
                googleSignInAccount,
                fitnessOption)
        }
        else
            startReadingData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GOOGLE_REQUESTE_CODE)
        {
            if(resultCode == RESULT_OK)
                startReadingData()
            else
                Toast.makeText(mActivity, "Google oauth failed", Toast.LENGTH_LONG).show()
        }
    }


    private fun getTodayData() {
        Fitness.getHistoryClient(mActivity, getGoogleAccount())
            .readDailyTotal(TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener(this)
        Fitness.getHistoryClient(mActivity, getGoogleAccount())
            .readDailyTotal(TYPE_CALORIES_EXPENDED)
            .addOnSuccessListener(this)
        Fitness.getHistoryClient(mActivity, getGoogleAccount())
            .readDailyTotal(TYPE_DISTANCE_DELTA)
            .addOnSuccessListener(this)
    }

    private fun requestForHistory()
    {
        val cal = Calendar.getInstance(Locale.GERMANY)
        cal.time = Date()
        val endTime = cal.timeInMillis

        cal.set(2021,2,1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        val startTime = cal.timeInMillis

        val readRequest = DataReadRequest.Builder()
            .aggregate(TYPE_STEP_COUNT_DELTA)
            .aggregate(AGGREGATE_STEP_COUNT_DELTA)
            .aggregate(TYPE_CALORIES_EXPENDED)
            .aggregate(AGGREGATE_CALORIES_EXPENDED)
            .aggregate(TYPE_DISTANCE_DELTA)
            .aggregate(AGGREGATE_DISTANCE_DELTA)
            .bucketByTime(1, TimeUnit.DAYS)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .build()

        Fitness.getHistoryClient(mActivity, getGoogleAccount())
            .readData(readRequest)
            .addOnSuccessListener(this)
    }

    private fun getGoogleAccount(): GoogleSignInAccount = GoogleSignIn.getAccountForExtension(mActivity, fitnessOption)

    private fun startReadingData() {
        getTodayData()
        subscribeAndGetRealTimeData(TYPE_STEP_COUNT_DELTA)
    }

    private fun subscribeAndGetRealTimeData(typeStepCountDelta: DataType) {
        Fitness.getRecordingClient(mActivity, getGoogleAccount())
            .subscribe(typeStepCountDelta)
            .addOnSuccessListener {
                Log.d("subscribeGetTimeData","Subscribed")
            }
            .addOnFailureListener {
                Log.d("subscribeGetTimeData","addOnFailureListener")
            }
        getDataUsingSensor(typeStepCountDelta)
    }

    override fun onSuccess(anyObject: Any?) {
        if(anyObject is DataSet)
        {
            val dataSet = anyObject as DataSet
            if(dataSet != null)
                getDataFromDataSet(dataSet)
        }
        else
            if(anyObject is DataReadResponse)
            {
                fitnessDataResponseModel.steps = 0f
                fitnessDataResponseModel.calories = 0f
                fitnessDataResponseModel.distance = 0f
                val dataReadResponse = anyObject as DataReadResponse
                val bucketList = dataReadResponse.buckets
                if(bucketList != null && bucketList.isNotEmpty())
                {
                    for(bucket in bucketList)
                    {
                        getDataFromDataReadResponse(bucket.getDataSet(TYPE_STEP_COUNT_DELTA))
                        getDataFromDataReadResponse(bucket.getDataSet(TYPE_CALORIES_EXPENDED))
                        getDataFromDataReadResponse(bucket.getDataSet(TYPE_DISTANCE_DELTA))
                    }
                    showDataHistory()
                }
            }
    }

    private fun getDataFromDataReadResponse(dataSet: DataSet?) {
        if(dataSet == null)
            return
        for(dataPoint in dataSet.dataPoints)
        {
            for(field in dataPoint.dataType.fields)
            {
                val value = dataPoint.getValue(field).toString().toFloat()
                if(field.name.equals(Field.FIELD_STEPS.name))
                    fitnessDataResponseModel.steps = DecimalFormat("#.##").format(value).toFloat()
                else {
                    if (field.name.equals(Field.FIELD_CALORIES.name))
                        fitnessDataResponseModel.calories =
                            DecimalFormat("#.##").format(value).toFloat()
                    else
                        if(field.name.equals(Field.FIELD_DISTANCE.name))
                            fitnessDataResponseModel.distance = DecimalFormat("#.##").format(value).toFloat()
                }
            }
        }
    }

    private fun getDataFromDataSet(dataSet: DataSet) {
        for(dataPoint in dataSet.dataPoints)
        {
            for(field in dataPoint.dataType.fields)
            {
                val value = dataPoint.getValue(field).toString().toFloat()
                if(field.name.equals(Field.FIELD_STEPS.name))
                    fitnessDataResponseModel.steps = DecimalFormat("#.##").format(value).toFloat()
                else {
                    if (field.name.equals(Field.FIELD_CALORIES.name))
                        fitnessDataResponseModel.calories =
                            DecimalFormat("#.##").format(value).toFloat()
                    else
                        if(field.name.equals(Field.FIELD_DISTANCE.name))
                            fitnessDataResponseModel.distance = DecimalFormat("#.##").format(value).toFloat()
                }
            }
        }
        showData()
    }

    private fun showData()
    {
        viewBinding.stepsTextView.text = "Steps = ${fitnessDataResponseModel.steps.toString()}"
        viewBinding.caloriesTextView.text = "Calories = ${fitnessDataResponseModel.calories.toString()}"
        viewBinding.distanceTextView.text = "Distance = ${fitnessDataResponseModel.distance.toString()}"
    }

    private fun showDataHistory()
    {
        viewBinding.stepsHistoryTextView.text = "Steps History = ${fitnessDataResponseModel.steps.toString()}"
        viewBinding.caloriesHistoryTextView.text = "Calories History = ${fitnessDataResponseModel.calories.toString()}"
        viewBinding.distanceHistoryTextView.text = "Distance History = ${fitnessDataResponseModel.distance.toString()}"
    }

    private fun getDataUsingSensor(dataType: DataType)
    {
        Fitness.getSensorsClient(mActivity, getGoogleAccount())
            .add(SensorRequest.Builder()
                    .setDataType(dataType)
                .setSamplingRate(1, TimeUnit.SECONDS)
                .build(),
                OnDataPointListener{dataPoint ->
                    val value = dataPoint.getValue(Field.FIELD_STEPS).toString().toFloat()
                    fitnessDataResponseModel.steps = DecimalFormat("#.##").format(value).toFloat()
                    viewBinding.stepsTextView.text = "Steps = ${fitnessDataResponseModel.steps.toString()}"
                }
            )
    }

}