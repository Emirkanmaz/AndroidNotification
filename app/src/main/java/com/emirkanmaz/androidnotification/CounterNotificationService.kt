package com.emirkanmaz.androidnotification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class CounterNotificationService(
    private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager

    fun showNotification(counter: Int){
//        intent tanımla notife tıklanınca ne olacağını bildiriye ata
        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            //minsdk 24 den büyük olduğu için gerek yok
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
            PendingIntent.FLAG_IMMUTABLE
        )

        val incrementIntent = PendingIntent.getBroadcast(
            context,
            2,
            Intent(context, CounterNotificationReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, COUNTER_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_heart_broken_24)
            .setContentTitle("Counter")
            .setContentText("Count is $counter")
            .setContentIntent(activityPendingIntent)
            .addAction(
                R.drawable.baseline_heart_broken_24,
                "Increment",
                incrementIntent
            )
            .build()

        notificationManager.notify(
            1,
            notification
        )
    }

    companion object{
        const val COUNTER_CHANNEL_ID = "counter_channel"
    }
}