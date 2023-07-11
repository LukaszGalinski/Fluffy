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
import com.lukasz.galinski.fluffy.presentation.common.toHtmlSpan

private const val SUMMARY_ID = 0
private const val GROUP_NOTIFICATION_FROM_VALUE = 0

class NotificationBuilder(private val context: Context) {
    private var notificationGroup = "GROUP_KEY_SUBSCRIPTION"
    private var notificationChanel = "CHANEL_KEY_SUBSCRIPTION"
    private var notificationName = "NOTIFICATION_NAME_SUBSCRIPTION"
    private var notificationDescription = "Chanel for subscription notification"

    private var notificationTypeId = 0
    private var notificationContentTitle = ""
    private var notificationContentText = context.getString(R.string.subscription_notification_content_text)
    private var notificationSummaryText = context.getString(R.string.subscription_notification_summary_text)
    private var notificationSummaryContentTitle =
        context.getString(R.string.subscription_notification_summary_content_title)

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

    fun setNotificationContentTitle(contentTitle: String): NotificationBuilder {
        this.notificationContentTitle = contentTitle
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

    fun setNotificationTypeId(notificationTypeId: Int): NotificationBuilder {
        this.notificationTypeId = notificationTypeId
        return this
    }

    private fun createSingleNotification(): Notification {
        return createBaseNotification()
            .setContentTitle(notificationContentTitle.toHtmlSpan())
            .setContentText(notificationContentText)
            .build()
    }

    private fun createGroupedNotification(): Notification {
        return createBaseNotification()
            .setStyle(
                NotificationCompat.InboxStyle()
                    .setSummaryText(notificationSummaryText)
                    .setBigContentTitle(notificationSummaryContentTitle)
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
                notify(notificationTypeId, createSingleNotification())
                if (activeNotifications.size > GROUP_NOTIFICATION_FROM_VALUE) notify(
                    SUMMARY_ID,
                    createGroupedNotification()
                )
            }
        }
    }
}
