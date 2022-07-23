package st235.com.github.strictcanary.data.baseline

import androidx.annotation.WorkerThread

internal interface StrictCanaryBaselineReader {

    @WorkerThread
    fun read(baselineResource: BaselineResource): BaselineDocument

}