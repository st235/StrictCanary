package st235.com.github.strictcanary

import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.strictmode.Violation
import java.util.concurrent.Executors

class PoliciesEnforcer {

    private val violationsExecutor = Executors.newSingleThreadExecutor()

    private val threadViolationListener = ThreadViolationListener()
    private val vmViolationListener = VMViolationListener()

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

    private fun onNewViolation(v: Violation) {

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