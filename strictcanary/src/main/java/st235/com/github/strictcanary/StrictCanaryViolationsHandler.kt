package st235.com.github.strictcanary

import android.os.strictmode.Violation
import st235.com.github.strictcanary.data.asStrictPolicyViolation
import st235.com.github.strictcanary.data.baseline.StrictCanaryBaselineReader
import st235.com.github.strictcanary.utils.ViolationProcessor
import st235.com.github.strictcanary.utils.notifications.NotificationManager

internal class StrictCanaryViolationsHandler(
    private val strictCanaryDetectionRequest: StrictCanaryDetectionPolicy
) {

    private val violationProcessor = ViolationProcessor(
        context = strictCanaryDetectionRequest.context,
        detectionDescriptor = strictCanaryDetectionRequest.detectionDescriptor,
        baselineResource = strictCanaryDetectionRequest.baselineDescriptor?.resource,
        baselineReader = strictCanaryDetectionRequest.baselineDescriptor?.format?.let { format ->
            StrictCanaryBaselineReader.create(format, strictCanaryDetectionRequest.context)
        }
    )

    private val notificationManager = NotificationManager(
        context = strictCanaryDetectionRequest.context
    )

    fun handle(violation: Violation) {
        if (violationProcessor.shouldProcessViolation(violation)) {
            notificationManager.showNotificationFor(violation.asStrictPolicyViolation())
        }
    }

}