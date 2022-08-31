package st235.com.github.strictcanary.data.baseline

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import st235.com.github.strictcanary.data.utils.TestBaselineResource

@RunWith(Enclosed::class)
class StrictCanaryXmlBaselineReaderTest {

    @RunWith(ParameterizedRobolectricTestRunner::class)
    class ValidXmlReadTest internal constructor(
        private val path: String,
        private val expectedBaseline: BaselineDocument
    ) {

        companion object {
            @JvmStatic
            @ParameterizedRobolectricTestRunner.Parameters(name = "Read from: {0}")
            fun params() = listOf(
                arrayOf(
                    "xml/strictcanary_baseline_1.xml",
                    BaselineDocument(
                        issues = mapOf(
                            "DiskRead" to listOf(
                                EntryIssue(null, "a"),
                                EntryIssue(null, "b"),
                                FileIssue(null, "c"),
                            )
                        )
                    )
                ),
                arrayOf(
                    "xml/strictcanary_baseline_2.xml",
                    BaselineDocument(
                        issues = mapOf(
                            "DiskRead" to listOf(
                                EntryIssue(null, "a"),
                                EntryIssue(null, "b"),
                                FileIssue(null, "c"),
                            ),
                            "DiskWrite" to listOf(
                                EntryIssue(null, "a*"),
                                FileIssue(null, "/b"),
                                EntryIssue(null, "*b"),
                            )
                        )
                    )
                ),
                arrayOf(
                    "xml/strictcanary_baseline_with_messages.xml",
                    BaselineDocument(
                        issues = mapOf(
                            "DiskRead" to listOf(
                                EntryIssue("A was ignored", "a"),
                                EntryIssue(null, "b"),
                                FileIssue("C was ignored", "c"),
                            ),
                            "DiskWrite" to listOf(
                                EntryIssue(null, "a*"),
                                FileIssue(null, "/b"),
                                EntryIssue(null, "*b"),
                            )
                        )
                    )
                ),
            )
        }

        private lateinit var context: Context
        private lateinit var strictCanaryXmlBaselineReader: StrictCanaryXmlBaselineReader

        @Before
        fun setUp() {
            context = InstrumentationRegistry.getInstrumentation().targetContext
            strictCanaryXmlBaselineReader = StrictCanaryXmlBaselineReader(context)
        }

        @Test
        fun `test that parser reads xml just fine`() {
            val file = TestBaselineResource(path)
            val baseline = strictCanaryXmlBaselineReader.read(file)
            assertEquals(expectedBaseline, baseline)
        }
    }

    @RunWith(ParameterizedRobolectricTestRunner::class)
    class InvalidXmlReadTest internal constructor(
        private val path: String
    ) {

        companion object {
            @JvmStatic
            @ParameterizedRobolectricTestRunner.Parameters(name = "Read from: {0}")
            fun params() = listOf(
                arrayOf("xml/invalid_strictcanary_baseline_1.xml"),
                arrayOf("xml/invalid_strictcanary_baseline_2.xml"),
                arrayOf("xml/invalid_strictcanary_baseline_3.xml"),
                arrayOf("xml/invalid_strictcanary_baseline_4.xml"),
                arrayOf("xml/invalid_strictcanary_baseline_5.xml"),
            )
        }

        private lateinit var context: Context
        private lateinit var strictCanaryXmlBaselineReader: StrictCanaryXmlBaselineReader

        @Before
        fun setUp() {
            context = InstrumentationRegistry.getInstrumentation().targetContext
            strictCanaryXmlBaselineReader = StrictCanaryXmlBaselineReader(context)
        }

        @Test(expected = IllegalStateException::class)
        fun `test that parser reads xml just fine`() {
            val file = TestBaselineResource(path)
            val baseline = strictCanaryXmlBaselineReader.read(file)
        }
    }

}
