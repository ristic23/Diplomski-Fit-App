package com.fit.diplomski.app.ui.dashboard

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fit.diplomski.app.ActivityRecognitionLogAdapter
import com.fit.diplomski.app.MainActivity.Companion.BROADCAST_DETECTED_ACTIVITY
import com.fit.diplomski.app.MainActivity.Companion.CONFIDENCE
import com.fit.diplomski.app.R
import com.fit.diplomski.app.databinding.FragmentDashboardBinding
import com.fit.diplomski.app.intentService.BackgroundDetectedActivitiesService
import com.fit.diplomski.app.model.RecognitionLogData
import com.google.android.gms.location.DetectedActivity
import java.text.SimpleDateFormat
import java.util.*

class DashboardFragment : Fragment() {

    private var dashboardBinding: FragmentDashboardBinding? = null
    private val viewBinding get() = dashboardBinding!!

    val ActivityCode = 5423

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var broadcastReceiver: BroadcastReceiver

    private lateinit var activityRecognitionLogAdapter: ActivityRecognitionLogAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        dashboardBinding = FragmentDashboardBinding.inflate(inflater, container, false)

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == BROADCAST_DETECTED_ACTIVITY) {
                    val type = intent.getIntExtra("type", -1)
                    val confidence = intent.getIntExtra("confidence", 0)
                    if (confidence > CONFIDENCE)
                        handleUserActivity(type, confidence)
                }
            }
        }
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(broadcastReceiver, IntentFilter(BROADCAST_DETECTED_ACTIVITY))
        }

        startTracking()

        activityRecognitionLogAdapter = ActivityRecognitionLogAdapter(ArrayList<RecognitionLogData>())
        viewBinding.activityRecognitionRV.apply {
            adapter = activityRecognitionLogAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }

        return viewBinding.root
    }

    private fun handleUserActivity(type: Int, confidence: Int) {
        var label = getString(R.string.activity_unknown)
        when (type) {
            DetectedActivity.IN_VEHICLE -> label = "You are in Vehicle"

            DetectedActivity.ON_BICYCLE -> label = "You are on Bicycle"

            DetectedActivity.ON_FOOT -> label = "You are on Foot"

            DetectedActivity.RUNNING -> label = "You are Running"

            DetectedActivity.STILL -> label = "You are Still"

            DetectedActivity.TILTING -> label = "Your phone is Tilted"

            DetectedActivity.WALKING -> label = "You are Walking"

            DetectedActivity.UNKNOWN -> label = "Unknown Activity"

            else -> label = "Unknown Activity"

        }

        viewBinding.progressBar.visibility= View.GONE
        viewBinding.tvResult.text = label
        //txtConfidence?.text = "Confidence: $confidence"
        val calendar: Calendar = Calendar.getInstance()
//        val date = String.format("%02d.%02d.%s", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR))
        val time = String.format("%02d:%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND))
        activityRecognitionLogAdapter.addNewActivity(RecognitionLogData(time, label))
        Log.i("Jovan Ristic Fit APp", "User activity: $label, Confidence: $confidence")
    }

    private fun startTracking() {
        val intent = Intent(activity, BackgroundDetectedActivitiesService::class.java)
        activity?.startService(intent)
    }

    private fun stopTracking() {
        val intent = Intent(activity, BackgroundDetectedActivitiesService::class.java)
        activity?.stopService(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(broadcastReceiver)
        }
        stopTracking()
    }


}