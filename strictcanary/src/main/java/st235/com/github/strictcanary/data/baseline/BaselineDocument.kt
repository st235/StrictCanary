package st235.com.github.strictcanary.data.baseline

import st235.com.github.strictcanary.data.StrictCanaryViolation
import st235.com.github.strictcanary.data.StrictCanaryViolationEntry
import st235.com.github.strictcanary.data.description

internal data class BaselineDocument(
    val issues: Map<String, List<Issue>>
) {

    fun contains(violation: StrictCanaryViolation): Boolean {
        val violationTypeInternalId = violation.type.id
        val ignoredIssues: List<Issue> = issues.getOrDefault(violationTypeInternalId, emptyList())

        for (issue in ignoredIssues) {
            for (entry in violation.violationEntriesStack) {
                if (issue.shouldIgnore(entry)) {
                    return true
                }
            }
        }

        return false
    }

    companion object {
        val EMPTY = BaselineDocument(issues = emptyMap())
    }
}

internal sealed interface Issue {

    val message: String?

    fun shouldIgnore(entry: StrictCanaryViolationEntry): Boolean

}

internal class FileIssue(
    override val message: String?,
    private val filePath: String
): Issue {

    override fun shouldIgnore(entry: StrictCanaryViolationEntry): Boolean {
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

        if (message != other.message) return false
        if (filePath != other.filePath) return false

        return true
    }

    override fun hashCode(): Int {
        var result = message?.hashCode() ?: 0
        result = 31 * result + filePath.hashCode()
        return result
    }


}

internal class EntryIssue(
    override val message: String?,
    private val codeEntry: String
): Issue {

    override fun shouldIgnore(entry: StrictCanaryViolationEntry): Boolean {
        val codeRegex = Regex(codeEntry)
        val description = entry.description
        return description.contains(codeRegex)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EntryIssue

        if (message != other.message) return false
        if (codeEntry != other.codeEntry) return false

        return true
    }

    override fun hashCode(): Int {
        var result = message?.hashCode() ?: 0
        result = 31 * result + codeEntry.hashCode()
        return result
    }


}
