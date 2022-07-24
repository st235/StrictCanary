package st235.com.github.strictcanary

import android.content.Context
import android.os.strictmode.Violation
import androidx.annotation.WorkerThread
import st235.com.github.strictcanary.data.StrictPolicyViolation.Type.Companion.isMaskedBy
import st235.com.github.strictcanary.data.asStrictPolicyViolation
import st235.com.github.strictcanary.data.baseline.BaselineDocument
import st235.com.github.strictcanary.data.baseline.BaselineResource
import st235.com.github.strictcanary.data.baseline.StrictCanaryBaselineReader
import st235.com.github.strictcanary.data.hasMyPackageEntries
import st235.com.github.strictcanary.utils.notifications.StrictPolicyNotificationManager

internal class ViolationProcessor(
    private val context: Context,
    private val shouldDetectThirdPartyViolations: Boolean,
    private val detectionMask: Int,
    private val baselineResource: BaselineResource?,
    private val baselineReader: StrictCanaryBaselineReader?,
    private val notificationManager: StrictPolicyNotificationManager
) {

    private val baseLine: BaselineDocument by lazy {
        return@lazy if (baselineResource == null || baselineReader == null) {
            BaselineDocument.EMPTY
        } else {
            baselineReader.read(baselineResource)
        }
    }

    @WorkerThread
    fun process(violation: Violation) {
        val strictPolicyViolation = violation.asStrictPolicyViolation()

        val type = strictPolicyViolation.type

        if (!type.isMaskedBy(detectionMask)) {
            return
        }

        if (!shouldDetectThirdPartyViolations && !strictPolicyViolation.hasMyPackageEntries(context)) {
            return
        }

        if (baseLine.contains(strictPolicyViolation)) {
            return
        }

        // handle
        notificationManager.showNotificationFor(strictPolicyViolation)
    }

}