package com.gimpel.diary.detail

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.gimpel.diary.data.DiaryEntry
import com.gimpel.diary.geofence.GeofenceBroadcastReceiver
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AddGeofenceUseCase @Inject constructor(
    @ApplicationContext val context: Context,
){
    operator fun invoke(entries: List<DiaryEntry>) {
        val geofenceList = entries.map { entry ->
            Geofence.Builder()
                .setRequestId(entry.id)
                .setCircularRegion(
                    entry.locationLatitude,
                    entry.locationLongitude,
                    1000f // 1km radius
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setLoiteringDelay(1)
                .build()
        }

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .apply {
                geofenceList.forEach { addGeofence(it) }
            }
            .build()

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getGeofencingClient(context)
                .addGeofences(geofencingRequest, geofencePendingIntent)
        }
    }

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(
            context,
            0, // Request code (can be any unique integer)
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE // Use FLAG_MUTABLE for Android 12+
        )
    }
}