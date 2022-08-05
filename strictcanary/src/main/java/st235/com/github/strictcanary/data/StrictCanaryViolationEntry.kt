package st235.com.github.strictcanary.data

import android.content.Context
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class StrictCanaryViolationEntry(
    val fileName: String?,
    val className: String,
    val methodName: String,
    val line: Int,
    val isNative: Boolean,
    val isMyPackage: Boolean
) : Parcelable

internal val StrictCanaryViolationEntry.description: String
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
