package st235.com.github.strictcanary

import android.content.Context
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.strictmode.Violation
import androidx.annotation.WorkerThread
import java.util.concurrent.Executors
import st235.com.github.strictcanary.data.baseline.BaselineFormat
import st235.com.github.strictcanary.data.baseline.BaselineResource
import st235.com.github.strictcanary.data.baseline.StrictCanaryBaselineReader
import st235.com.github.strictcanary.data.baseline.StrictCanaryXmlBaselineReader
import st235.com.github.strictcanary.utils.assertNotOnMainThread

internal class PoliciesEnforcer(
    context: Context,
    detectionMask: Int,
    baselineResource: BaselineResource?,
    baselineFormat: BaselineFormat?
) {

    private val violationsExecutor = Executors.newSingleThreadExecutor()

    private val threadViolationListener = ThreadViolationListener()
    private val vmViolationListener = VMViolationListener()

    private val violationProcessor = ViolationProcessor(
        detectionMask = detectionMask,
        baselineResource = baselineResource,
        baselineReader = baselineFormat?.let { format ->
            StrictCanaryBaselineReader.create(format, context)
        }
    )

    init {
        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder()
                .detectAll()
                .penaltyListener(violationsExecutor, threadViolationListener)
                .build()
        )

        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyListener(violationsExecutor, vmViolationListener)
                .build()
        )
    }

    @WorkerThread
    private fun onNewViolation(violation: Violation) {
        assertNotOnMainThread()
        violationProcessor.process(violation)
    }

    private inner class ThreadViolationListener: StrictMode.OnThreadViolationListener {

        override fun onThreadViolation(v: Violation?) {
            if (v == null) {
                return
            }

            onNewViolation(v)
        }
    }

    private inner class VMViolationListener: StrictMode.OnVmViolationListener {

        override fun onVmViolation(v: Violation?) {
            if (v == null) {
                return
            }

            onNewViolation(v)
        }
    }

}