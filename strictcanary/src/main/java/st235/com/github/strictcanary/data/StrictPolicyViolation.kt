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
import st235.com.github.strictcanary.utils.asStrictPolicyViolationEntry

@Parcelize
internal data class StrictPolicyViolation(
    val id: Int,
    val type: Type,
    val violationEntriesStack: List<StrictPolicyViolationEntry>
): Parcelable {

    enum class Type(
        val id: String,
        val mask: Int
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
        CREADENTIAL_LEAKS(
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

        companion object {
            const val EXPLICIT_GC_KEY = "explicitgc"
        }
    }
}

internal fun Violation.asStrictPolicyViolation(): StrictPolicyViolation {
    val violationEntriesStack = stackTrace.map { it.asStrictPolicyViolationEntry() }.toList()

    val id = violationEntriesStack.hashCode()

    return StrictPolicyViolation(
        id = id,
        type = type,
        violationEntriesStack = violationEntriesStack
    )
}

private val Violation.type: StrictPolicyViolation.Type
    get() {
        val matchedTypeByClass = matchWithTypeByClass()

        if (matchedTypeByClass != null) {
            return matchedTypeByClass
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (this is ImplicitDirectBootViolation) {
                return StrictPolicyViolation.Type.IMPLICIT_DIRECT_BOOT
            } else if (this is CredentialProtectedWhileLockedViolation) {
                return StrictPolicyViolation.Type.CREADENTIAL_LEAKS
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (this is IncorrectContextUseViolation) {
                return StrictPolicyViolation.Type.INCORRECT_CONTEXT_USE
            } else if (this is UnsafeIntentLaunchViolation) {
                return StrictPolicyViolation.Type.UNSAFE_INTENT_LAUNCH
            }
        }

        val firstStackTraceEntry = stackTrace.firstOrNull()?.toString()

        // match test api calls
        if (firstStackTraceEntry?.contains(
                StrictPolicyViolation.Type.EXPLICIT_GC_KEY,
                ignoreCase = true
            ) == true
        ) {
            return StrictPolicyViolation.Type.EXPLICIT_GC
        }

        return StrictPolicyViolation.Type.UNKNOWN
    }

private fun Violation.matchWithTypeByClass(): StrictPolicyViolation.Type? {
    return when (this) {
        is DiskReadViolation -> StrictPolicyViolation.Type.DISK_READ
        is DiskWriteViolation -> StrictPolicyViolation.Type.DISK_WRITE
        is NetworkViolation -> StrictPolicyViolation.Type.NETWORK
        is CustomViolation -> StrictPolicyViolation.Type.CUSTOM_SLOW_CALLS
        is ResourceMismatchViolation -> StrictPolicyViolation.Type.RESOURCE_MISMATCH
        is UnbufferedIoViolation -> StrictPolicyViolation.Type.UNBUFFERED_IO
        is CleartextNetworkViolation -> StrictPolicyViolation.Type.CLEARTEXT_NETWORK
        is ContentUriWithoutPermissionViolation -> StrictPolicyViolation.Type.CONTENT_URI_WITHOUT_PERMISSION
        is FileUriExposedViolation -> StrictPolicyViolation.Type.FILE_URI_EXPOSURE
        is LeakedClosableViolation -> StrictPolicyViolation.Type.CLOSABLE_LEAKS
        is SqliteObjectLeakedViolation -> StrictPolicyViolation.Type.SQL_OBJECT_LEAKS
        is NonSdkApiUsedViolation -> StrictPolicyViolation.Type.NON_SDK_API_USAGE
        is UntaggedSocketViolation -> StrictPolicyViolation.Type.UNTAGGED_SOCKET
        is InstanceCountViolation -> StrictPolicyViolation.Type.INSTANCE_COUNT
        is IntentReceiverLeakedViolation -> StrictPolicyViolation.Type.INTENT_RECEIVER_LEAKS
        else -> null
    }
}
