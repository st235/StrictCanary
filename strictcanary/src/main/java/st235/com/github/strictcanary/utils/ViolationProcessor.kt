package st235.com.github.strictcanary.utils

import android.content.Context
import android.os.strictmode.Violation
import androidx.annotation.WorkerThread
import st235.com.github.strictcanary.data.StrictCanaryViolation.Type.Companion.isMaskedBy
import st235.com.github.strictcanary.data.asStrictPolicyViolation
import st235.com.github.strictcanary.data.baseline.BaselineDocument
import st235.com.github.strictcanary.data.baseline.BaselineResource
import st235.com.github.strictcanary.data.baseline.StrictCanaryBaselineReader
import st235.com.github.strictcanary.data.detection.DetectionDescriptor
import st235.com.github.strictcanary.data.hasMyPackageEntries

internal class ViolationProcessor(
    private val context: Context,
    private val detectionDescriptor: DetectionDescriptor,
    private val baselineResource: BaselineResource?,
    private val baselineReader: StrictCanaryBaselineReader?
) {

    private val baseLine: BaselineDocument by lazy {
        return@lazy if (baselineResource == null || baselineReader == null) {
            BaselineDocument.EMPTY
        } else {
            baselineReader.read(baselineResource)
        }
    }

    @WorkerThread
    fun shouldProcessViolation(violation: Violation): Boolean {
        val strictPolicyViolation = violation.asStrictPolicyViolation()

        val type = strictPolicyViolation.type

        return when {
            !type.isMaskedBy(detectionDescriptor.mask) -> false
            detectionDescriptor.shouldSkipThirdPartyLibraries &&
                    !strictPolicyViolation.hasMyPackageEntries(context) -> false
            baseLine.contains(strictPolicyViolation) -> false
            else -> true
        }
    }

}