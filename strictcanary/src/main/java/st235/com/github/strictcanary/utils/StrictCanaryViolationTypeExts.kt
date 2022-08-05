package st235.com.github.strictcanary.utils

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AppsOutage
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.DeviceUnknown
import androidx.compose.material.icons.rounded.DoorBack
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
import st235.com.github.strictcanary.data.StrictCanaryViolation

@get:StringRes
internal val StrictCanaryViolation.Type.localisedTitleRes: Int
get() {
    return when(this) {
        StrictCanaryViolation.Type.DISK_READ -> R.string.strict_canary_type_title_disk_read
        StrictCanaryViolation.Type.DISK_WRITE -> R.string.strict_canary_type_title_disk_write
        StrictCanaryViolation.Type.NETWORK -> R.string.strict_canary_type_title_network
        StrictCanaryViolation.Type.CUSTOM_SLOW_CALLS -> R.string.strict_canary_type_title_custom_slow_calls
        StrictCanaryViolation.Type.RESOURCE_MISMATCH -> R.string.strict_canary_type_title_resource_mismatch
        StrictCanaryViolation.Type.UNBUFFERED_IO -> R.string.strict_canary_type_title_unbuffered_io
        StrictCanaryViolation.Type.EXPLICIT_GC -> R.string.strict_canary_type_title_explicit_gc
        StrictCanaryViolation.Type.SQL_OBJECT_LEAKS -> R.string.strict_canary_type_title_sql_object_leaks
        StrictCanaryViolation.Type.CLOSABLE_LEAKS -> R.string.strict_canary_type_title_closable_leaks
        StrictCanaryViolation.Type.INTENT_RECEIVER_LEAKS -> R.string.strict_canary_type_title_intent_receiver_leaks
        StrictCanaryViolation.Type.INSTANCE_COUNT -> R.string.strict_canary_type_title_instance_count
        StrictCanaryViolation.Type.CREDENTIAL_LEAKS -> R.string.strict_canary_type_title_credentials_leaks
        StrictCanaryViolation.Type.FILE_URI_EXPOSURE -> R.string.strict_canary_type_title_file_uri_exposure
        StrictCanaryViolation.Type.CLEARTEXT_NETWORK -> R.string.strict_canary_type_title_cleartext_network
        StrictCanaryViolation.Type.CONTENT_URI_WITHOUT_PERMISSION -> R.string.strict_canary_type_title_content_uri_without_permission
        StrictCanaryViolation.Type.UNTAGGED_SOCKET -> R.string.strict_canary_type_title_untagged_socket
        StrictCanaryViolation.Type.NON_SDK_API_USAGE -> R.string.strict_canary_type_title_non_sdk_api_usage
        StrictCanaryViolation.Type.IMPLICIT_DIRECT_BOOT -> R.string.strict_canary_type_title_implicit_direct_boot
        StrictCanaryViolation.Type.INCORRECT_CONTEXT_USE -> R.string.strict_canary_type_title_incorrect_context_use
        StrictCanaryViolation.Type.UNSAFE_INTENT_LAUNCH -> R.string.strict_canary_type_title_unsafe_intent_launch
        StrictCanaryViolation.Type.UNKNOWN -> R.string.strict_canary_type_title_unknown
    }
}

internal val StrictCanaryViolation.Type.vectorIcon: ImageVector
get() {
    return when(this) {
        StrictCanaryViolation.Type.DISK_READ -> Icons.Rounded.Book
        StrictCanaryViolation.Type.DISK_WRITE -> Icons.Rounded.Save
        StrictCanaryViolation.Type.NETWORK -> Icons.Rounded.Public
        StrictCanaryViolation.Type.CUSTOM_SLOW_CALLS -> Icons.Rounded.Speed
        StrictCanaryViolation.Type.RESOURCE_MISMATCH -> Icons.Rounded.Rule
        StrictCanaryViolation.Type.UNBUFFERED_IO -> Icons.Rounded.FileDownloadOff
        StrictCanaryViolation.Type.EXPLICIT_GC -> Icons.Rounded.Delete
        StrictCanaryViolation.Type.SQL_OBJECT_LEAKS -> Icons.Rounded.Storage
        StrictCanaryViolation.Type.CLOSABLE_LEAKS -> Icons.Rounded.DoorBack
        StrictCanaryViolation.Type.INTENT_RECEIVER_LEAKS -> Icons.Rounded.MarkunreadMailbox
        StrictCanaryViolation.Type.INSTANCE_COUNT -> Icons.Rounded.LooksOne
        StrictCanaryViolation.Type.CREDENTIAL_LEAKS -> Icons.Rounded.LockOpen
        StrictCanaryViolation.Type.FILE_URI_EXPOSURE -> Icons.Rounded.FolderOpen
        StrictCanaryViolation.Type.CLEARTEXT_NETWORK -> Icons.Rounded.Wifi
        StrictCanaryViolation.Type.CONTENT_URI_WITHOUT_PERMISSION -> Icons.Rounded.Description
        StrictCanaryViolation.Type.UNTAGGED_SOCKET -> Icons.Rounded.LocalOffer
        StrictCanaryViolation.Type.NON_SDK_API_USAGE -> Icons.Rounded.PedalBike
        StrictCanaryViolation.Type.IMPLICIT_DIRECT_BOOT -> Icons.Rounded.RestartAlt
        StrictCanaryViolation.Type.INCORRECT_CONTEXT_USE -> Icons.Rounded.AppsOutage
        StrictCanaryViolation.Type.UNSAFE_INTENT_LAUNCH -> Icons.Rounded.Launch
        StrictCanaryViolation.Type.UNKNOWN -> Icons.Rounded.DeviceUnknown
    }
}
