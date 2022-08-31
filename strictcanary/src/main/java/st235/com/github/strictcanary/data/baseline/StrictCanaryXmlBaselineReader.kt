package st235.com.github.strictcanary.data.baseline

import android.content.Context
import kotlin.IllegalStateException
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import st235.com.github.strictcanary.data.StrictCanaryViolation

/**
 * <strict-canary>
 *     <issue id="IssueId">
 *         <ignore path="file path" />
 *         <ignore>code</ignore>
 *     </issue>
 * </strict-canary>
 */
internal class StrictCanaryXmlBaselineReader(
    private val context: Context
): StrictCanaryBaselineReader {

    private companion object {
        const val KEY_ROOT_STRICT_CANARY = "strict-canary"
        const val KEY_ISSUE = "issue"
        const val ARG_ISSUE_ID = "id"
        const val KEY_IGNORE = "ignore"
        const val ARG_IGNORE_PATH = "path"
        const val ARG_MESSAGE = "message"
    }

    private val issuesIds: Set<String> by lazy {
        val ids = mutableSetOf<String>()

        for (type in StrictCanaryViolation.Type.values()) {
            ids.add(type.id)
        }

        return@lazy ids
    }

    private val xmlParsersFactory = XmlPullParserFactory.newInstance()

    override fun read(baselineResource: BaselineResource): BaselineDocument {
        val xmlParser = xmlParsersFactory.newPullParser()
        xmlParser.setInput(baselineResource.open(context),  null)

        if (xmlParser.eventType == XmlPullParser.START_DOCUMENT) {
            xmlParser.nextIgnoringText()
        }

        val baselines = readAllBaselineDocuments(xmlParser)

        if (baselines.size != 1) {
            throw IllegalStateException("Expected 1 baseline root entry but found ${baselines.size}")
        }

        return baselines.first()
    }

    private fun readAllBaselineDocuments(xmlPullParser: XmlPullParser): List<BaselineDocument> {
        val baselines = mutableListOf<BaselineDocument>()

        while (xmlPullParser.eventType == XmlPullParser.START_TAG &&
            xmlPullParser.name == KEY_ROOT_STRICT_CANARY) {

            xmlPullParser.nextIgnoringText()
            val baseline = readBaselineDocument(xmlPullParser)

            baselines.add(baseline)

            if (xmlPullParser.eventType != XmlPullParser.END_TAG ||
                    xmlPullParser.name != KEY_ROOT_STRICT_CANARY) {
                throw IllegalStateException("Cannot find closing tag for <$KEY_ROOT_STRICT_CANARY>")
            }

            xmlPullParser.nextIgnoringText()
        }

        if (baselines.size > 1) {
            throw IllegalStateException("More than one <$KEY_ROOT_STRICT_CANARY> entries")
        }

        return baselines
    }

    private fun readBaselineDocument(xmlPullParser: XmlPullParser): BaselineDocument {
        val baseline = mutableMapOf<String, List<Issue>>()

        while (xmlPullParser.eventType == XmlPullParser.START_TAG &&
            xmlPullParser.name == KEY_ISSUE) {
            val id: String? = xmlPullParser.getAttributeValue(null, ARG_ISSUE_ID)

            if (id == null) {
                throw IllegalStateException("Issue id cannot be null")
            }

            if (!issuesIds.contains(id)) {
                throw IllegalStateException("Unknown id: $id")
            }

            if (baseline.containsKey(id)) {
                throw IllegalStateException("$id has been declared more than once")
            }

            xmlPullParser.nextIgnoringText()
            val issues = readIssues(xmlPullParser)

            baseline[id] = issues

            if (xmlPullParser.eventType != XmlPullParser.END_TAG ||
                xmlPullParser.name != KEY_ISSUE) {
                throw IllegalStateException("Cannot find closing tag for <$KEY_ISSUE>")
            }

            xmlPullParser.nextIgnoringText()
        }

        return BaselineDocument(
            issues = baseline
        )
    }

    private fun readIssues(xmlPullParser: XmlPullParser): List<Issue> {
        val issues = mutableListOf<Issue>()

        while (xmlPullParser.eventType == XmlPullParser.START_TAG &&
            xmlPullParser.name == KEY_IGNORE) {
            val message: String? = xmlPullParser.getAttributeValue(null, ARG_MESSAGE)
            val path: String? = xmlPullParser.getAttributeValue(null, ARG_IGNORE_PATH)

            if (path == null) {
                xmlPullParser.nextIgnoringText()

                if (xmlPullParser.eventType != XmlPullParser.TEXT) {
                    throw IllegalStateException("Expected text but found ${xmlPullParser.eventType}")
                }

                issues.add(EntryIssue(message, xmlPullParser.text))

                xmlPullParser.nextIgnoringText()
            } else {
                issues.add(FileIssue(message, path))
                xmlPullParser.nextIgnoringText()
            }

            if (xmlPullParser.eventType != XmlPullParser.END_TAG ||
                xmlPullParser.name != KEY_IGNORE) {
                throw IllegalStateException("Cannot find closing tag for <$KEY_IGNORE>")
            }

            xmlPullParser.nextIgnoringText()
        }

        return issues
    }

    private fun XmlPullParser.nextIgnoringText() {
        next()

        while (eventType == XmlPullParser.TEXT && text.isNullOrBlank()) {
            next()
        }
    }
}