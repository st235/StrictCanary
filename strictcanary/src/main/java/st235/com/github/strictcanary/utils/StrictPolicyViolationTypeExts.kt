package st235.com.github.strictcanary.utils

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AppsOutage
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.DeviceUnknown
import androidx.compose.material.icons.rounded.DoorBack
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.FileDownloadOff
import androidx.compose.material.icons.rounded.FolderOpen
import androidx.compose.material.icons.rounded.Launch
import androidx.compose.material.icons.rounded.LocalOffer
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.LooksOne
import androidx.compose.material.icons.rounded.MarkunreadMailbox
import androidx.compose.material.icons.rounded.PedalBike
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.Rule
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material.icons.rounded.Storage
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.ui.graphics.vector.ImageVector
import st235.com.github.strictcanary.R
import st235.com.github.strictcanary.data.StrictPolicyViolation

@get:StringRes
internal val StrictPolicyViolation.Type.localisedTitleRes: Int
get() {
    return when(this) {
        StrictPolicyViolation.Type.DISK_READ -> R.string.strict_canary_type_title_disk_read
        StrictPolicyViolation.Type.DISK_WRITE -> R.string.strict_canary_type_title_disk_write
        StrictPolicyViolation.Type.NETWORK -> R.string.strict_canary_type_title_network
        StrictPolicyViolation.Type.CUSTOM_SLOW_CALLS -> R.string.strict_canary_type_title_custom_slow_calls
        StrictPolicyViolation.Type.RESOURCE_MISMATCH -> R.string.strict_canary_type_title_resource_mismatch
        StrictPolicyViolation.Type.UNBUFFERED_IO -> R.string.strict_canary_type_title_unbuffered_io
        StrictPolicyViolation.Type.EXPLICIT_GC -> R.string.strict_canary_type_title_explicit_gc
        StrictPolicyViolation.Type.SQL_OBJECT_LEAKS -> R.string.strict_canary_type_title_sql_object_leaks
        StrictPolicyViolation.Type.CLOSABLE_LEAKS -> R.string.strict_canary_type_title_closable_leaks
        StrictPolicyViolation.Type.INTENT_RECEIVER_LEAKS -> R.string.strict_canary_type_title_intent_receiver_leaks
        StrictPolicyViolation.Type.INSTANCE_COUNT -> R.string.strict_canary_type_title_instance_count
        StrictPolicyViolation.Type.CREDENTIAL_LEAKS -> R.string.strict_canary_type_title_credentials_leaks
        StrictPolicyViolation.Type.FILE_URI_EXPOSURE -> R.string.strict_canary_type_title_file_uri_exposure
        StrictPolicyViolation.Type.CLEARTEXT_NETWORK -> R.string.strict_canary_type_title_cleartext_network
        StrictPolicyViolation.Type.CONTENT_URI_WITHOUT_PERMISSION -> R.string.strict_canary_type_title_content_uri_without_permission
        StrictPolicyViolation.Type.UNTAGGED_SOCKET -> R.string.strict_canary_type_title_untagged_socket
        StrictPolicyViolation.Type.NON_SDK_API_USAGE -> R.string.strict_canary_type_title_non_sdk_api_usage
        StrictPolicyViolation.Type.IMPLICIT_DIRECT_BOOT -> R.string.strict_canary_type_title_implicit_direct_boot
        StrictPolicyViolation.Type.INCORRECT_CONTEXT_USE -> R.string.strict_canary_type_title_incorrect_context_use
        StrictPolicyViolation.Type.UNSAFE_INTENT_LAUNCH -> R.string.strict_canary_type_title_unsafe_intent_launch
        StrictPolicyViolation.Type.UNKNOWN -> R.string.strict_canary_type_title_unknown
    }
}

internal val StrictPolicyViolation.Type.vectorIcon: ImageVector
get() {
    return when(this) {
        StrictPolicyViolation.Type.DISK_READ -> Icons.Rounded.Book
        StrictPolicyViolation.Type.DISK_WRITE -> Icons.Rounded.Save
        StrictPolicyViolation.Type.NETWORK -> Icons.Rounded.Public
        StrictPolicyViolation.Type.CUSTOM_SLOW_CALLS -> Icons.Rounded.Speed
        StrictPolicyViolation.Type.RESOURCE_MISMATCH -> Icons.Rounded.Rule
        StrictPolicyViolation.Type.UNBUFFERED_IO -> Icons.Rounded.FileDownloadOff
        StrictPolicyViolation.Type.EXPLICIT_GC -> Icons.Rounded.Delete
        StrictPolicyViolation.Type.SQL_OBJECT_LEAKS -> Icons.Rounded.Storage
        StrictPolicyViolation.Type.CLOSABLE_LEAKS -> Icons.Rounded.DoorBack
        StrictPolicyViolation.Type.INTENT_RECEIVER_LEAKS -> Icons.Rounded.MarkunreadMailbox
        StrictPolicyViolation.Type.INSTANCE_COUNT -> Icons.Rounded.LooksOne
        StrictPolicyViolation.Type.CREDENTIAL_LEAKS -> Icons.Rounded.LockOpen
        StrictPolicyViolation.Type.FILE_URI_EXPOSURE -> Icons.Rounded.FolderOpen
        StrictPolicyViolation.Type.CLEARTEXT_NETWORK -> Icons.Rounded.Wifi
        StrictPolicyViolation.Type.CONTENT_URI_WITHOUT_PERMISSION -> Icons.Rounded.Description
        StrictPolicyViolation.Type.UNTAGGED_SOCKET -> Icons.Rounded.LocalOffer
        StrictPolicyViolation.Type.NON_SDK_API_USAGE -> Icons.Rounded.PedalBike
        StrictPolicyViolation.Type.IMPLICIT_DIRECT_BOOT -> Icons.Rounded.RestartAlt
        StrictPolicyViolation.Type.INCORRECT_CONTEXT_USE -> Icons.Rounded.AppsOutage
        StrictPolicyViolation.Type.UNSAFE_INTENT_LAUNCH -> Icons.Rounded.Launch
        StrictPolicyViolation.Type.UNKNOWN -> Icons.Rounded.DeviceUnknown
    }
}
