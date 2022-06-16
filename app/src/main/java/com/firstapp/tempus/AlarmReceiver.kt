package com.firstapp.tempus

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {

    // When this class is called through receiver
    override fun onReceive(context: Context, intent: Intent) {

        // create explicit intent of main activity
        val mainIntent = Intent(context, MainActivity::class.java)
        //  create pending intent
        val pendingIntent = TaskStackBuilder.create(context).run {
            // add intent to back stack
            addNextIntentWithParentStack(mainIntent)
            // get intent
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        // create notification
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Notification Title")
            .setContentText("this is the content text")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        // notify
        val  manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }
}