package st235.com.github.strictcanary.data.baseline

import android.content.Context
import androidx.annotation.WorkerThread

internal interface StrictCanaryBaselineReader {

    @WorkerThread
    fun read(baselineResource: BaselineResource): BaselineDocument

    companion object {
        fun create(baselineFormat: BaselineFormat, context: Context): StrictCanaryBaselineReader {
            return when(baselineFormat) {
                BaselineFormat.XML -> StrictCanaryXmlBaselineReader(context)
            }
        }
    }

}