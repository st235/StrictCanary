package st235.com.github.strictcanary.data

import android.content.Context
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class StrictPolicyViolationEntry(
    val fileName: String?,
    val className: String,
    val methodName: String,
    val line: Int,
    val isNative: Boolean
) : Parcelable

internal val StrictPolicyViolationEntry.description: String
    get() {
        val result = StringBuilder()
        result.append(className).append(".").append(methodName)
        if (isNative) {
            result.append("(Native Method)")
        } else if (fileName != null) {
            if (line >= 0) {
                result.append("(").append(fileName).append(":").append(line).append(")")
            } else {
                result.append("(").append(fileName).append(")")
            }
        } else {
            if (line >= 0) {
                // The line number is actually the dex pc.
                result.append("(Unknown Source:").append(line).append(")")
            } else {
                result.append("(Unknown Source)")
            }
        }
        return result.toString()
    }

internal fun StrictPolicyViolationEntry.isMyPackage(context: Context): Boolean {
    val packageName = context.packageName
    return description.contains(packageName, ignoreCase = true)
}
