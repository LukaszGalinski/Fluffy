package com.lukasz.galinski.fluffy.presentation

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.lukasz.galinski.fluffy.R

var i = 0

class NotificationBuilder(private val context: Context, private val chanelId: String = "SubscriptionChanel") :
    NotificationCompat.Builder(context, chanelId) {

    private fun summaryNotification() {

    }

    fun createNotification() {
        val builder = NotificationCompat.Builder(context, "CHANNEL")
            .setStyle(NotificationCompat.InboxStyle()
                .setBigContentTitle("2 new messages")
                .setSummaryText("janedoe@example.com"))
            .setSmallIcon(R.drawable.icon_shop)
            .setContentTitle("textTitle")
            .setContentText("textContent")
            .setChannelId("CHANNEL")
            .setAutoCancel(true)
            .setGroup("SUBSCRIPTION")

        val channel = NotificationChannel("CHANNEL",
            "Subscription",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Chanel description"
        }


        val notificationManager: NotificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        i++


        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (notificationManager.activeNotifications.size < 3){
                    notify(i, builder.build())
                    return
                }
                notify(i, builder.build())
                notify(99, )

            }
        }
    }
}