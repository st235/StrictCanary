package st235.com.github.strictcanary.utils

import androidx.annotation.WorkerThread
import st235.com.github.strictcanary.data.StrictCanaryViolation
import st235.com.github.strictcanary.data.StrictCanaryViolation.Type.Companion.isMaskedBy
import st235.com.github.strictcanary.data.UnprocessedStrictCanaryViolation
import st235.com.github.strictcanary.data.asBaselinedPartyViolation
import st235.com.github.strictcanary.data.asWhitelistedViolation
import st235.com.github.strictcanary.data.baseline.BaselineDocument
import st235.com.github.strictcanary.data.baseline.BaselineResource
import st235.com.github.strictcanary.data.baseline.StrictCanaryBaselineReader
import st235.com.github.strictcanary.data.detection.DetectionDescriptor
import st235.com.github.strictcanary.data.hasMyPackageEntries

internal class ViolationProcessor(
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
    fun process(violation: UnprocessedStrictCanaryViolation): StrictCanaryViolation {
        val type = violation.type

        val shouldProcessViolation = when {
            !type.isMaskedBy(detectionDescriptor.mask) -> false
            detectionDescriptor.shouldSkipThirdPartyLibraries &&
                    !violation.hasMyPackageEntries -> false
            baseLine.contains(violation) -> false
            else -> true
        }

        return if (!shouldProcessViolation) {
            violation.asBaselinedPartyViolation()
        } else {
            violation.asWhitelistedViolation()
        }
    }

}