package st235.com.github.strictcanary.data.detection

import androidx.annotation.IntRange

internal data class DetectionDescriptor(
    @IntRange(from = 0L)
    internal val mask: Int,
    internal val shouldSkipThirdPartyLibraries: Boolean
)