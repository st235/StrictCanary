package st235.com.github.strictcanary.utils

import android.os.Looper

internal fun assertNotOnMainThread() {
    val onMainThread = Looper.getMainLooper().isCurrentThread
    if (onMainThread) {
        throw IllegalStateException("Code should not run on main thread")
    }
}
