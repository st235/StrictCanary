package st235.com.github.strictcanary

import android.content.Context
import st235.com.github.strictcanary.data.StrictPolicyViolation
import st235.com.github.strictcanary.data.baseline.BaselineFormat

/**
 * No op class
 */
class StrictCanary private constructor(
    request: Request
) {


    class Request(
        internal val context: Context
    ) {

        fun shouldDetectThirdPartyViolations(shouldDetectThirdPartyViolations: Boolean): Request {
            return this
        }

        fun assetsBaseline(path: String, baselineFormat: BaselineFormat = BaselineFormat.XML): Request {
            return this
        }

        fun rawBaseline(rawId: Int, baselineFormat: BaselineFormat = BaselineFormat.XML): Request {
            return this
        }

        fun detect(type: StrictPolicyViolation.Type): Request {
            return this
        }

        fun build(): StrictCanary {
            return StrictCanary(this)
        }

    }

}