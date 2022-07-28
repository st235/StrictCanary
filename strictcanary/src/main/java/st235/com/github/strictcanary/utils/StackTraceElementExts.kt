package st235.com.github.strictcanary.utils

import st235.com.github.strictcanary.data.StrictCanaryViolationEntry

internal fun StackTraceElement.asStrictPolicyViolationEntry(): StrictCanaryViolationEntry {
    return StrictCanaryViolationEntry(
        fileName = this.fileName,
        className = this.className,
        methodName = this.methodName,
        line = this.lineNumber,
        isNative = this.isNativeMethod
    )
}
