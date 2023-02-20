package st235.com.github.strictcanary

import android.content.Context
import st235.com.github.strictcanary.data.StrictCanaryViolation
import st235.com.github.strictcanary.data.baseline.BaselineFormat

/**
 * No-op detection policy
 */
class StrictCanaryDetectionPolicy {

    class Builder(
        internal val context: Context
    ) {

        fun shouldSkipThirdPartyLibraries(shouldSkipThirdPartyLibraries: Boolean): Builder {
            return this
        }

        fun assetsBaseline(path: String, baselineFormat: BaselineFormat = BaselineFormat.XML): Builder {
            return this
        }

        fun rawBaseline(rawId: Int, baselineFormat: BaselineFormat = BaselineFormat.XML): Builder {
            return this
        }

        fun showEveryViolationAsSeparateNotification(): Builder {
            return this
        }

        fun showAllViolationsAtOnce(): Builder {
            return this
        }

        fun detect(type: StrictCanaryViolation.Type): Builder {
            return this
        }

        fun build(): StrictCanaryDetectionPolicy {
            return StrictCanaryDetectionPolicy()
        }

    }

}
