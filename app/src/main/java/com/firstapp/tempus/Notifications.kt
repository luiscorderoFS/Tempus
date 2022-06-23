package com.firstapp.tempus

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import java.util.*

val CHANNEL_ID = "channelID"
val CHANNEL_NAME = "Event notifications"
val NOTIFICATION_TITLE = "NotificationTitle"
val NOTIFICATION_ID = ""

class Notifications {
    // Create companion object to be able to create an instance of this class
    companion object Factory {
        fun create(): Notifications = Notifications()
    }
    // Schedule Notification
    fun scheduleNotification(context: Context, eventObj: Event) {
        // Create receiver for when app is killed
        val receiver = ComponentName(context, AlarmReceiver::class.java)
        context.packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
        // Create intent to AlarmReceiver class
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(NOTIFICATION_TITLE, eventObj.mTitle)
        intent.putExtra(NOTIFICATION_ID, eventObj.mNumID.toInt())
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            eventObj.mNumID.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        // Create AlarmManager
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // Set alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                eventObj.mTimeInMillis,
                pendingIntent
            )
        }
    }
    // Create Notification Channel
    fun createNotificationChannel(context: Context) {
        // if api >= 26, requires notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // build notification channel
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            // create system notification manager
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // create notification channel
            manager.createNotificationChannel(channel)
        }
    }
}