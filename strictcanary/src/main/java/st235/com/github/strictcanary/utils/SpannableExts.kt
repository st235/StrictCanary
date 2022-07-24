package st235.com.github.strictcanary.utils

import android.text.Spannable

internal fun <T : Spannable> T.applySpanForEntireString(
    what: Any,
    flag: Int = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
) {
    setSpan(what, 0, length, flag)
}
