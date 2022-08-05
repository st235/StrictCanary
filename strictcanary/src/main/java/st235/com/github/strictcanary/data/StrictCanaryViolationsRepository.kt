package st235.com.github.strictcanary.data

import st235.com.github.strictcanary.utils.LruHashSet

internal class StrictCanaryViolationsRepository private constructor() {

    companion object {

        /**
         * As we don't have any
         * di framework here, let's use
         * singleton pattern.
         */
        val INSTANCE = StrictCanaryViolationsRepository()

        private const val MAX_CACHE_SIZE = 150L

    }

    interface Listener {

        fun onNewViolation(strictCanaryViolation: StrictCanaryViolation)

    }

    private val lruCache = LruHashSet<StrictCanaryViolation>(MAX_CACHE_SIZE)

    val snapshot: List<StrictCanaryViolation>
    get() {
        return lruCache.snapshot
    }

    var listener: Listener? = null

    fun add(strictCanaryViolation: StrictCanaryViolation) {
        lruCache.add(strictCanaryViolation)
        listener?.onNewViolation(strictCanaryViolation)
    }

}