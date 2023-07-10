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

class NotificationBuilder(private val context: Context) {
    private var notificationGroup = "GROUP_KEY_SUBSCRIPTION"
    private var notificationChanel = "CHANEL_KEY_SUBSCRIPTION"
    private var notificationName = "NOTIFICATION_NAME_SUBSCRIPTION"
    private var notificationDescription = "Chanel for subscription notification"
    private var notificationTypeId = 0
    private var notificationServiceName = "UNKNOWN"
    private var notificationTitle = "subscription"

    private lateinit var notificationManager: NotificationManager

    fun createChanel(): NotificationBuilder {
        notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        NotificationChannel(notificationChanel, notificationName, NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = notificationDescription
            notificationManager.createNotificationChannel(this)
        }
        return this
    }

    private fun createBaseNotification(): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, notificationChanel)
            .setSmallIcon(R.drawable.ic_baseline_dollar_24)
            .setChannelId(notificationChanel)
            .setGroup(notificationGroup)
    }

    fun setSubscriptionType(subscriptionType: String): NotificationBuilder {
        this.notificationServiceName = subscriptionType
        return this
    }

    fun setNotificationGroup(notificationGroup: String): NotificationBuilder {
        this.notificationGroup = notificationGroup
        return this
    }

    fun setNotificationChanel(notificationChanel: String): NotificationBuilder {
        this.notificationChanel = notificationChanel
        return this
    }

    fun setNotificationName(notificationName: String): NotificationBuilder {
        this.notificationName = notificationName
        return this
    }

    fun setNotificationDescription(notificationDescription: String): NotificationBuilder {
        this.notificationDescription = notificationDescription
        return this
    }

    fun setSubscriptionTitle(subscriptionTitle: String): NotificationBuilder {
        this.notificationTitle = subscriptionTitle
        return this
    }

    fun setNotificationTypeId(notificationTypeId: Int): NotificationBuilder {
        this.notificationTypeId = notificationTypeId
        return this
    }

    private fun createSingleNotification(): Notification {
        return createBaseNotification()
            .setSubText("$notificationServiceName $notificationTitle")
            .setContentTitle("$notificationServiceName subscription expire in less than 24h.")
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
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.apply {
                val notificationCount = activeNotifications.size
                notify(notificationTypeId, createSingleNotification())
                if (notificationCount > 0) notify(SUMMARY_ID, createGroupedNotification(notificationCount))
            }
        }
    }
}