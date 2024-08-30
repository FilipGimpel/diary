package com.gimpel.diary.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.gimpel.diary.detail.SendNotificationUseCase
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class GeofenceBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var sendNotificationUseCase: SendNotificationUseCase

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("GeofenceBroadcast", "Received geofence event")

        val geofencingEvent = GeofencingEvent.fromIntent(intent) ?: return

        if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                geofencingEvent.triggeringGeofences?.let { geofences ->
                    sendNotificationUseCase(geofences.map { it.requestId })
                }
                pendingResult.finish()
            }
        }
    }
}