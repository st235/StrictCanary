package st235.com.github.strictcanary

import android.os.StrictMode
import android.os.strictmode.Violation
import androidx.annotation.WorkerThread
import java.util.concurrent.Executors
import st235.com.github.strictcanary.utils.assertNotOnMainThread

object StrictCanary {

    private val rootViolationsListener = object : ViolationsListener {

        override fun onNewViolation(violation: Violation) {
            assertNotOnMainThread()
            strictCanaryViolationsHandler?.handle(violation)
        }

    }

    private val violationsExecutor = Executors.newSingleThreadExecutor()

    private val threadViolationListener = ThreadViolationListener(rootViolationsListener)
    private val vmViolationListener = VMViolationListener(rootViolationsListener)

    @Volatile
    private var strictCanaryViolationsHandler: StrictCanaryViolationsHandler? = null

    init {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
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

    fun setDetectionPolicy(detectionRequest: StrictCanaryDetectionPolicy) {
        strictCanaryViolationsHandler = StrictCanaryViolationsHandler(
            strictCanaryDetectionRequest = detectionRequest
        )
    }

    private class ThreadViolationListener(
        private val violationsListener: ViolationsListener
    ): StrictMode.OnThreadViolationListener {

        override fun onThreadViolation(v: Violation?) {
            if (v == null) {
                return
            }

            violationsListener.onNewViolation(v)
        }
    }

    private class VMViolationListener(
        private val violationsListener: ViolationsListener
    ): StrictMode.OnVmViolationListener {

        override fun onVmViolation(v: Violation?) {
            if (v == null) {
                return
            }

            violationsListener.onNewViolation(v)
        }
    }

    private interface ViolationsListener {

        @WorkerThread
        fun onNewViolation(violation: Violation)

    }

}