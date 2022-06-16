package com.firstapp.tempus

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.*

val CHANNEL_ID = "channelID"
val CHANNEL_NAME = "Event notifications"
val NOTIFICATION_TITLE = "NotificationTitle"
val NOTIFICATION_ID = 1

class Notifications {
    // Create companion object to be able to create an instance of this class
    companion object Factory {
        fun create(): Notifications = Notifications()
    }
    // Schedule Notification
    fun scheduleNotification(context: Context, title: String, time: Long) {
        // Create intent to AlarmReceiver
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(NOTIFICATION_TITLE, title)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        // Create AlarmManager
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // Set alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
        }
    }
    // Create Notification Channel
    fun createNotificationChannel(context: Context) {
        // if api >= 26, requires notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // build notification channel
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH)
            //.apply {
            // do whatever with notification

            //}
            // create system notification manager
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // create notification channel
            manager.createNotificationChannel(channel)
        }
    }
}