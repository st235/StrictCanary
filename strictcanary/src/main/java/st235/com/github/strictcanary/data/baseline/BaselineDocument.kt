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
    fun shouldIgnore(entry: StrictCanaryViolationEntry): Boolean
}

internal class FileIssue(
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

    override fun shouldIgnore(entry: StrictCanaryViolationEntry): Boolean {
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
