package st235.com.github.strictcanary.data.baseline

import android.content.Context
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import st235.com.github.strictcanary.data.utils.TestBaselineResource

@RunWith(RobolectricTestRunner::class)
class StrictCanaryXmlBaselineReaderTest {

    private val context = mock<Context>()
    private val strictCanaryXmlBaselineReader = StrictCanaryXmlBaselineReader(context)

    @Test
    fun `test that xml read is fine`() {
        val file = TestBaselineResource("xml/strictcanary_baseline_1.xml")
        val baseline = strictCanaryXmlBaselineReader.read(file)

        val expectedBaseline = BaselineDocument(
            issues = mapOf(
                "DiskRead" to listOf(
                    EntryIssue("a"),
                    EntryIssue("b"),
                    FileIssue("c")
                )
            )
        )

        assertEquals(expectedBaseline, baseline)
    }
}