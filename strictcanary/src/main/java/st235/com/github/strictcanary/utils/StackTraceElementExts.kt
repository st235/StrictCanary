package st235.com.github.strictcanary.utils

import st235.com.github.strictcanary.data.StrictPolicyViolationEntry

internal fun StackTraceElement.asStrictPolicyViolationEntry(): StrictPolicyViolationEntry {
    return StrictPolicyViolationEntry(
        fileName = this.fileName,
        className = this.className,
        methodName = this.methodName,
        line = this.lineNumber,
        isNative = this.isNativeMethod
    )
}
