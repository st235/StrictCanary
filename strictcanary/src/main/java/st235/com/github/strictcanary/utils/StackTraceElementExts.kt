package st235.com.github.strictcanary.utils

import st235.com.github.strictcanary.data.StrictCanaryViolationEntry

internal fun StackTraceElement.asStrictPolicyViolationEntry(
    packageName: String
): StrictCanaryViolationEntry {
    return StrictCanaryViolationEntry(
        fileName = this.fileName,
        className = this.className,
        methodName = this.methodName,
        line = this.lineNumber,
        isNative = this.isNativeMethod,
        isMyPackage = this.hasMyPackage(packageName)
    )
}

private fun StackTraceElement.hasMyPackage(packageName: String): Boolean {
    return toString().contains(packageName, ignoreCase = true)
}
