package com.fit.diplomski.app.activityRecognition.intentService

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.fit.diplomski.app.MainActivity
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity

class DetectedActivitiesIntentService : IntentService(TAG) {

    override fun onHandleIntent(intent: Intent?) {
        if(intent == null)
            return
        val result = ActivityRecognitionResult.extractResult(intent) ?: return
        val detectedActivities = result.probableActivities as ArrayList<*>
        for (activity in detectedActivities) {
            broadcastActivity(activity as DetectedActivity)
        }
    }

    private fun broadcastActivity(activity: DetectedActivity) {
        val intent = Intent(MainActivity.BROADCAST_DETECTED_ACTIVITY)
        intent.putExtra("type", activity.type)
        intent.putExtra("confidence", activity.confidence)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    companion object {
        val TAG = DetectedActivitiesIntentService::class.java.simpleName
    }

}