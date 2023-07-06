package com.lukasz.galinski.fluffy.presentation

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.lukasz.galinski.fluffy.R

private const val SUMMARY_ID = 0

class SubscriptionNotification(private val context: Context) {
    private val notificationGroup = "SUBSCRIPTIONS"
    private val notificationChanel = "subscriptionChanel"
    private val notificationDescription = "Chanel for subscription notification"
    private val notificationName = "Subscription"
    private var subscriptionNotificationTypeId = 0
    private var subscriptionType = "UNKNOWN"
    private val subscriptionTitle = " subscription expire soon!"

    private lateinit var notificationManager: NotificationManager

    init {
        createChanel()
    }

    private fun createChanel() {
        notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        NotificationChannel(notificationChanel, notificationName, NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = notificationDescription
            notificationManager.createNotificationChannel(this)
        }
    }

    private fun createBaseNotification(): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, notificationChanel)
            .setSmallIcon(R.drawable.ic_baseline_dollar_24)
            .setChannelId(notificationChanel)
            .setGroup(notificationGroup)

    }

    fun setSubscriptionType(subscriptionType: String): SubscriptionNotification {
        this.subscriptionType = subscriptionType
        return this
    }

    fun setNotificationTypeId(notificationTypeId: Int): SubscriptionNotification {
        this.subscriptionNotificationTypeId = notificationTypeId
        return this
    }

    private fun createSingleNotification(): Notification {
        return createBaseNotification()
            .setSubText(subscriptionType + subscriptionTitle)
            .setContentTitle("$subscriptionType subscription expire in less than 24h.")
            .setContentText("Remember to renew or cancel your membership!")
            .build()
    }

    private fun createGroupedNotification(subscriptionsNumber: Int): Notification {
        return createBaseNotification()
            .setStyle(
                NotificationCompat.InboxStyle()
                    .setSummaryText("$subscriptionsNumber Subscriptions expire soon!")
                    .setBigContentTitle("Your subscriptions will end soon. Please renew or cancel")
            )
            .setSmallIcon(R.drawable.ic_baseline_dollar_24)
            .setGroupSummary(true)
            .build()
    }

    fun build() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.apply {
                val notificationCount = activeNotifications.size
                notify(subscriptionNotificationTypeId, createSingleNotification())
                if (notificationCount > 0) notify(SUMMARY_ID, createGroupedNotification(notificationCount))
            }
        }
    }
}