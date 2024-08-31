package com.gimpel.diary.domain

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.gimpel.diary.R
import com.gimpel.diary.data.FirestoreRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SendNotificationUseCase @Inject constructor(
    @ApplicationContext val context: Context, private val fireStoreRepository: FirestoreRepository
) {
    private val notificationChannelId = "GEOFENCE_CHANNEL_ID"

    suspend operator fun invoke(diaryEntryIds: List<String>) {
        diaryEntryIds.forEach { id ->
            val diaryEntry = fireStoreRepository.getDiaryEntry(id)

            sendNotification(
                context,
                context.getString(R.string.geofence_alert_notification_title),
                context.getString(R.string.geofence_alert_notification_message, diaryEntry.title)
            )
        }
    }

    private fun sendNotification(context: Context, title: String, content: String) {
        createNotificationChannel(context)

        val notificationBuilder = buildNotification(context, title, content)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(
            System.currentTimeMillis().toInt(), notificationBuilder.build()
        )
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel_geofence_name)
            val descriptionText = context.getString(R.string.notification_channel_geofence_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(notificationChannelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(
        context: Context, title: String, message: String
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, notificationChannelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title).setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_MAX)
    }
}