package st235.com.github.strictcanary.utils

fun String.leftEllipsize(maxCharacters: Int): String {
    if (this.length > maxCharacters) {
        return "..." + substring(length - maxCharacters + 3, length)
    }

    return this
}
