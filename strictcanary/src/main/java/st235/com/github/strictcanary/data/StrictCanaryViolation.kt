package st235.com.github.strictcanary.data

import android.os.Build
import android.os.Parcelable
import android.os.strictmode.CleartextNetworkViolation
import android.os.strictmode.ContentUriWithoutPermissionViolation
import android.os.strictmode.CredentialProtectedWhileLockedViolation
import android.os.strictmode.CustomViolation
import android.os.strictmode.DiskReadViolation
import android.os.strictmode.DiskWriteViolation
import android.os.strictmode.FileUriExposedViolation
import android.os.strictmode.ImplicitDirectBootViolation
import android.os.strictmode.IncorrectContextUseViolation
import android.os.strictmode.InstanceCountViolation
import android.os.strictmode.IntentReceiverLeakedViolation
import android.os.strictmode.LeakedClosableViolation
import android.os.strictmode.NetworkViolation
import android.os.strictmode.NonSdkApiUsedViolation
import android.os.strictmode.ResourceMismatchViolation
import android.os.strictmode.SqliteObjectLeakedViolation
import android.os.strictmode.UnbufferedIoViolation
import android.os.strictmode.UnsafeIntentLaunchViolation
import android.os.strictmode.UntaggedSocketViolation
import android.os.strictmode.Violation
import kotlinx.parcelize.Parcelize
import st235.com.github.strictcanary.data.baseline.Issue
import st235.com.github.strictcanary.utils.asStrictPolicyViolationEntry

@Parcelize
data class StrictCanaryViolation internal constructor(
    internal val id: Int,
    internal val type: Type,
    internal val myPackageOffset: Int,
    internal val violationEntriesStack: List<StrictCanaryViolationEntry>,
    internal val baselineType: BaselineType,
    internal val baselineMessage: String?
): Parcelable {

    internal operator fun get(index: Int): StrictCanaryViolationEntry {
        return violationEntriesStack[index]
    }

    enum class Type(
        internal val id: String,
        internal val mask: Int
    ) {
        // thread policies violations
        DISK_READ(
            id = "DiskRead",
            mask = 1 shl 0
        ),
        DISK_WRITE(
            id = "DiskWrite",
            mask = 1 shl 1
        ),
        NETWORK(
            id = "Network",
            mask = 1 shl 2
        ),
        CUSTOM_SLOW_CALLS(
            id = "CustomSlowCalls",
            mask = 1 shl 3
        ),
        RESOURCE_MISMATCH(
            id = "ResourceMismatch",
            mask = 1 shl 4
        ),
        UNBUFFERED_IO(
            id = "UnbufferedIO",
            mask = 1 shl 5
        ),
        EXPLICIT_GC(
            id = "ExplicitGC",
            mask = 1 shl 6
        ),

        // vm policies violations
        SQL_OBJECT_LEAKS(
            id = "CursorLeaks",
            mask = 1 shl 7
        ),
        CLOSABLE_LEAKS(
            id = "ClosableLeaks",
            mask = 1 shl 8
        ),
        INTENT_RECEIVER_LEAKS(
            id = "ActivityLeaks",
            mask = 1 shl 9
        ),
        INSTANCE_COUNT(
            id = "InstanceLeaks",
            mask = 1 shl 10
        ),
        CREDENTIAL_LEAKS(
            id = "RegistrationLeaks",
            mask = 1 shl 11
        ),
        FILE_URI_EXPOSURE(
            id = "FileUriExposure",
            mask = 1 shl 12
        ),
        CLEARTEXT_NETWORK(
            id = "CleartextNetwork",
            mask = 1 shl 13
        ),
        CONTENT_URI_WITHOUT_PERMISSION(
            id = "ContentUriWithoutPermission",
            mask = 1 shl 14
        ),
        UNTAGGED_SOCKET(
            id = "UntaggedSocket",
            mask = 1 shl 15
        ),
        NON_SDK_API_USAGE(
            id = "NonSdkApiUsage",
            mask = 1 shl 16
        ),
        IMPLICIT_DIRECT_BOOT(
            id = "ImplicitDirectBoot",
            mask = 1 shl 17
        ),
        INCORRECT_CONTEXT_USE(
            id = "IncorrectContextUse",
            mask = 1 shl 18
        ),
        UNSAFE_INTENT_LAUNCH(
            id = "UnsafeIntentLaunch",
            mask = 1 shl 19
        ),

        // misc
        UNKNOWN(
            id = "Unknown",
            mask = 0
        );

        internal companion object {
            const val EXPLICIT_GC_KEY = "explicitgc"

            infix fun Type.with(other: Type): Int {
                return mask or other.mask
            }

            infix fun Int.with(other: Type): Int {
                return this or other.mask
            }

            fun Type.isMaskedBy(mask: Int): Boolean {
                return (mask and this.mask) != 0
            }
        }
    }

    internal enum class BaselineType {
        UNKNOWN,
        BASELINED,
        WHITELISTED
    }

}

internal fun Violation.asUnprocessedStrictPolicyViolation(
    myPackageName: String
): StrictCanaryViolation {
    val violationEntriesStack = stackTrace.map { it.asStrictPolicyViolationEntry(myPackageName) }.toList()
    val myPackageOffset = violationEntriesStack.indexOfFirst { it.isMyPackage }

    val id = violationEntriesStack.hashCode()

    return StrictCanaryViolation(
        id = id,
        type = type,
        myPackageOffset = myPackageOffset,
        violationEntriesStack = violationEntriesStack,
        baselineType = StrictCanaryViolation.BaselineType.UNKNOWN,
        baselineMessage = null
    )
}

private val Violation.type: StrictCanaryViolation.Type
    get() {
        val matchedTypeByClass = matchWithTypeByClass()

        if (matchedTypeByClass != null) {
            return matchedTypeByClass
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (this is ImplicitDirectBootViolation) {
                return StrictCanaryViolation.Type.IMPLICIT_DIRECT_BOOT
            } else if (this is CredentialProtectedWhileLockedViolation) {
                return StrictCanaryViolation.Type.CREDENTIAL_LEAKS
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (this is IncorrectContextUseViolation) {
                return StrictCanaryViolation.Type.INCORRECT_CONTEXT_USE
            } else if (this is UnsafeIntentLaunchViolation) {
                return StrictCanaryViolation.Type.UNSAFE_INTENT_LAUNCH
            }
        }

        val firstStackTraceEntry = stackTrace.firstOrNull()?.toString()

        // match test api calls
        if (firstStackTraceEntry?.contains(
                StrictCanaryViolation.Type.EXPLICIT_GC_KEY,
                ignoreCase = true
            ) == true
        ) {
            return StrictCanaryViolation.Type.EXPLICIT_GC
        }

        return StrictCanaryViolation.Type.UNKNOWN
    }

private fun Violation.matchWithTypeByClass(): StrictCanaryViolation.Type? {
    return when (this) {
        is DiskReadViolation -> StrictCanaryViolation.Type.DISK_READ
        is DiskWriteViolation -> StrictCanaryViolation.Type.DISK_WRITE
        is NetworkViolation -> StrictCanaryViolation.Type.NETWORK
        is CustomViolation -> StrictCanaryViolation.Type.CUSTOM_SLOW_CALLS
        is ResourceMismatchViolation -> StrictCanaryViolation.Type.RESOURCE_MISMATCH
        is UnbufferedIoViolation -> StrictCanaryViolation.Type.UNBUFFERED_IO
        is CleartextNetworkViolation -> StrictCanaryViolation.Type.CLEARTEXT_NETWORK
        is ContentUriWithoutPermissionViolation -> StrictCanaryViolation.Type.CONTENT_URI_WITHOUT_PERMISSION
        is FileUriExposedViolation -> StrictCanaryViolation.Type.FILE_URI_EXPOSURE
        is LeakedClosableViolation -> StrictCanaryViolation.Type.CLOSABLE_LEAKS
        is SqliteObjectLeakedViolation -> StrictCanaryViolation.Type.SQL_OBJECT_LEAKS
        is NonSdkApiUsedViolation -> StrictCanaryViolation.Type.NON_SDK_API_USAGE
        is UntaggedSocketViolation -> StrictCanaryViolation.Type.UNTAGGED_SOCKET
        is InstanceCountViolation -> StrictCanaryViolation.Type.INSTANCE_COUNT
        is IntentReceiverLeakedViolation -> StrictCanaryViolation.Type.INTENT_RECEIVER_LEAKS
        else -> null
    }
}

internal val StrictCanaryViolation.hasMyPackageEntries: Boolean
    get() {
        return myPackageOffset >= 0
    }

internal fun StrictCanaryViolation.asBaselinedViolation(
    baselineMessage: String?
): StrictCanaryViolation {
    return this.copy(
        baselineType = StrictCanaryViolation.BaselineType.BASELINED,
        baselineMessage = baselineMessage
    )
}

internal fun StrictCanaryViolation.asWhitelistedViolation(): StrictCanaryViolation {
    return this.copy(
        baselineType = StrictCanaryViolation.BaselineType.WHITELISTED,
        baselineMessage = null
    )
}
