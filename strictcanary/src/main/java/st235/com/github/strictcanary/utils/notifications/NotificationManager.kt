package st235.com.github.strictcanary.utils.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlin.math.min
import st235.com.github.strictcanary.R
import st235.com.github.strictcanary.data.StrictCanaryViolation
import st235.com.github.strictcanary.data.description
import st235.com.github.strictcanary.presentation.StrictCanaryActivity
import st235.com.github.strictcanary.utils.applySpanForEntireString
import st235.com.github.strictcanary.utils.localisedTitleRes

internal class NotificationManager(
    private val context: Context
) {

    private companion object {
        const val STRICT_CANARY_CHANNEL_ID = "notifications.channel.st235.strict_canary"

        const val DEFAULT_STACK_TRACE_OFFSET = 3
        const val NOTIFICATION_DESCRIPTION_LINES = 5
        const val NEW_LINE ='\n'
    }

    private val notificationManager = NotificationManagerCompat.from(context)

    fun showNotificationFor(violation: StrictCanaryViolation) {
        createChannelIfNecessary()

        val contentIntent = StrictCanaryActivity.createIntent(context, null)

        val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val pendingContentIntent = PendingIntent.getActivity(
            context,
            violation.id,
            contentIntent,
            pendingIntentFlags
        )

        val notification = NotificationCompat.Builder(context, STRICT_CANARY_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_strict_canary_small_outline)
            .setContentTitle(violation.notificationTitle)
            .setContentText(violation.notificationBriefDescription)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(violation.notificationLongDescription)
            )
            .setContentIntent(pendingContentIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(violation.id, notification)
    }

    private fun createChannelIfNecessary() {
        val name = context.getString(R.string.strict_canary_notification_channel_title)
        val descriptionText =
            context.getString(R.string.strict_canary_notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(STRICT_CANARY_CHANNEL_ID, name, importance)
        channel.description = descriptionText
        notificationManager.createNotificationChannel(channel)
    }

    private val StrictCanaryViolation.notificationTitle: CharSequence
        get() {
            val typeTitle = context.getString(type.localisedTitleRes)
            val rawText = context.getString(R.string.strict_canary_notification_title, typeTitle)

            val spannableText = SpannableString(rawText)
            spannableText.applySpanForEntireString(StyleSpan(Typeface.BOLD))

            return spannableText
        }

    private val StrictCanaryViolation.notificationBriefDescription: CharSequence
        get() {
            val firstMyPackageEntryOffset = myPackageOffset

            if (firstMyPackageEntryOffset == -1) {
                return violationEntriesStack[DEFAULT_STACK_TRACE_OFFSET].description
            }

            return violationEntriesStack[DEFAULT_STACK_TRACE_OFFSET].description
        }

    private val StrictCanaryViolation.notificationLongDescription: CharSequence
    get()
    {
        val firstMyPackageEntryOffset = myPackageOffset

        val offset = if (firstMyPackageEntryOffset == -1) {
            DEFAULT_STACK_TRACE_OFFSET
        } else {
            firstMyPackageEntryOffset
        }

        val builder = SpannableStringBuilder()

        for (i in offset until min(offset + NOTIFICATION_DESCRIPTION_LINES, violationEntriesStack.size)) {
            if (i > offset) builder.append(NEW_LINE)

            val entry = violationEntriesStack[i]

            val text = if (entry.isMyPackage) {
                val spannable = SpannableString(entry.description)
                spannable.applySpanForEntireString(StyleSpan(Typeface.BOLD))
                spannable
            } else {
                entry.description
            }

            builder.append(text)
        }

        return builder
    }

}