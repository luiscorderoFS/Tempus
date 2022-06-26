package com.firstapp.tempus

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    // When this class is called through receiver
    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            var auth: FirebaseAuth = Firebase.auth
            var db: FirebaseFirestore = Firebase.firestore
            db.collection("Users").document(auth.uid.toString()).collection("All Events").get()
                .addOnSuccessListener { result->
                    // For each document, if notification time is in the future, set notification
                    for (document in result) {
                        val eventObj = document.toObject<Event>()
                        if (eventObj.mTimeInMillis >= Calendar.getInstance().timeInMillis)
                            Notifications.create().scheduleNotification(context, eventObj)
                        else {
                            // Delete the document as it is not needed to check for notifications anymore
                            db.collection("Users").document(auth.uid.toString()).collection("All Events").document(document.id).delete()
                        }
                    }
                }
        }
        else {
            val title = intent.getStringExtra(NOTIFICATION_TITLE)
            val notificationID = intent.getIntExtra(NOTIFICATION_ID, 0)
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
                .setContentTitle(title)
                .setContentText("this is the content text")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
            // notify
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(notificationID, notification)
        }
    }
}