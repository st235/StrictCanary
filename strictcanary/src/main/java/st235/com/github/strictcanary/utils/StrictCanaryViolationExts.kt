package st235.com.github.strictcanary.utils

import st235.com.github.strictcanary.data.StrictCanaryViolation
import st235.com.github.strictcanary.data.StrictCanaryViolationEntry

internal data class ViolationClipBoardDescriptor(
    val type: String,
    val entry: String
)

internal val StrictCanaryViolation.headline: StrictCanaryViolationEntry
    get() {
        return this.violationEntriesStack.getOrNull(myPackageOffset)
            ?: this.violationEntriesStack.first()
    }

internal val StrictCanaryViolation.clipboardDescriptor: ViolationClipBoardDescriptor
    get() {
        val headline = this.headline

        val headlineDescription = StringBuilder()
            .append(headline.className)
            .append(".")
            .append(headline.methodName)
            .toString()

        return ViolationClipBoardDescriptor(
            type = this.type.id,
            entry = headlineDescription
        )
    }
