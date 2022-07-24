package st235.com.github.strictcanary.data

class StrictPolicyViolation {

    enum class Type {
        DISK_READ,
        DISK_WRITE,
        NETWORK,
        CUSTOM_SLOW_CALLS,
        RESOURCE_MISMATCH,
        UNBUFFERED_IO,
        EXPLICIT_GC,
        SQL_OBJECT_LEAKS,
        CLOSABLE_LEAKS,
        INTENT_RECEIVER_LEAKS,
        INSTANCE_COUNT,
        CREDENTIAL_LEAKS,
        FILE_URI_EXPOSURE,
        CLEARTEXT_NETWORK,
        CONTENT_URI_WITHOUT_PERMISSION,
        NON_SDK_API_USAGE,
        IMPLICIT_DIRECT_BOOT,
        INCORRECT_CONTEXT_USE,
        UNSAFE_INTENT_LAUNCH,
        UNKNOWN
    }
}
