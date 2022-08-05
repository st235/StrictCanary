package st235.com.github.strictcanary.utils

import st235.com.github.strictcanary.data.StrictCanaryViolation
import st235.com.github.strictcanary.data.StrictCanaryViolationEntry

internal val StrictCanaryViolation.headline: StrictCanaryViolationEntry
    get() {
        return this.violationEntriesStack.getOrNull(myPackageOffset)
            ?: this.violationEntriesStack.first()
    }
