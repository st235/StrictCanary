package st235.com.github.strictcanary

import android.content.Context
import android.os.strictmode.Violation
import st235.com.github.strictcanary.data.StrictCanaryViolation
import st235.com.github.strictcanary.data.StrictCanaryViolationsRepository
import st235.com.github.strictcanary.data.asUnprocessedStrictPolicyViolation
import st235.com.github.strictcanary.data.baseline.StrictCanaryBaselineReader
import st235.com.github.strictcanary.data.baselineType
import st235.com.github.strictcanary.utils.ViolationProcessor
import st235.com.github.strictcanary.utils.notifications.NotificationManager

internal class StrictCanaryViolationsHandler(
    private val strictCanaryDetectionRequest: StrictCanaryDetectionPolicy
) {

    private val violationProcessor = ViolationProcessor(
        detectionDescriptor = strictCanaryDetectionRequest.detectionDescriptor,
        baselineResource = strictCanaryDetectionRequest.baselineDescriptor?.resource,
        baselineReader = strictCanaryDetectionRequest.baselineDescriptor?.format?.let { format ->
            StrictCanaryBaselineReader.create(format, strictCanaryDetectionRequest.context)
        }
    )

    private val context: Context
    get() {
        return strictCanaryDetectionRequest.context
    }

    private val notificationManager = NotificationManager(
        context = strictCanaryDetectionRequest.context
    )

    private val strictCanaryViolationsRepository = StrictCanaryViolationsRepository.INSTANCE

    fun handle(violation: Violation) {
        val processedViolation = violationProcessor.process(
            violation.asUnprocessedStrictPolicyViolation(
                myPackageName = context.packageName
            )
        )

        if (processedViolation.baselineType == StrictCanaryViolation.BaselineType.WHITELISTED) {
            notificationManager.showNotificationFor(processedViolation, strictCanaryDetectionRequest.notificationStrategy)
        }

        strictCanaryViolationsRepository.add(processedViolation)
    }

}