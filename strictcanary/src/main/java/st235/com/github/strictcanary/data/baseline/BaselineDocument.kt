package st235.com.github.strictcanary.data.baseline

import st235.com.github.strictcanary.data.StrictPolicyViolationEntry
import st235.com.github.strictcanary.data.description

internal data class BaselineDocument(
    val issues: Map<String, List<Issue>>
)

internal sealed interface Issue {
    fun shouldIgnore(entry: StrictPolicyViolationEntry): Boolean
}

internal class FileIssue(
    private val filePath: String
): Issue {

    override fun shouldIgnore(entry: StrictPolicyViolationEntry): Boolean {
        val pathRegex = Regex(filePath)
        val fileName = entry.fileName

        if (fileName == null) {
            return false
        }

        return fileName.contains(pathRegex)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileIssue

        if (filePath != other.filePath) return false

        return true
    }

    override fun hashCode(): Int {
        return filePath.hashCode()
    }


}

internal class EntryIssue(
    private val codeEntry: String
): Issue {

    override fun shouldIgnore(entry: StrictPolicyViolationEntry): Boolean {
        val codeRegex = Regex(codeEntry)
        val description = entry.description
        return description.contains(codeRegex)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EntryIssue

        if (codeEntry != other.codeEntry) return false

        return true
    }

    override fun hashCode(): Int {
        return codeEntry.hashCode()
    }


}
