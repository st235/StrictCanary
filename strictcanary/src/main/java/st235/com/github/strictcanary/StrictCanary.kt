package st235.com.github.strictcanary

import android.content.Context
import androidx.annotation.RawRes
import st235.com.github.strictcanary.data.StrictPolicyViolation
import st235.com.github.strictcanary.data.baseline.AssetsBaselineResource
import st235.com.github.strictcanary.data.baseline.BaselineFormat
import st235.com.github.strictcanary.data.baseline.BaselineResource
import st235.com.github.strictcanary.data.baseline.RawBaselineResource

class StrictCanary private constructor(
    request: Request
) {

    private val context: Context
    private val policiesEnforcer: PoliciesEnforcer

    init {
        this.context = request.context
        this.policiesEnforcer = PoliciesEnforcer(
            context = request.context,
            detectionMask = request.detectionMask,
            baselineFormat = request.baselineFormat,
            baselineResource = request.baselineResource,
            shouldDetectThirdPartyViolations = request.shouldDetectThirdPartyViolations
        )
    }


    class Request(
        internal val context: Context
    ) {

        internal var shouldDetectThirdPartyViolations: Boolean = true
        internal var detectionMask: Int = 0
        internal var baselineResource: BaselineResource? = null
        internal var baselineFormat: BaselineFormat? = null

        fun shouldDetectThirdPartyViolations(shouldDetectThirdPartyViolations: Boolean): Request {
            this.shouldDetectThirdPartyViolations = shouldDetectThirdPartyViolations
            return this
        }

        fun assetsBaseline(path: String, baselineFormat: BaselineFormat = BaselineFormat.XML): Request {
            this.baselineFormat = baselineFormat
            this.baselineResource = AssetsBaselineResource(path)
            return this
        }

        fun rawBaseline(@RawRes rawId: Int, baselineFormat: BaselineFormat = BaselineFormat.XML): Request {
            this.baselineFormat = baselineFormat
            this.baselineResource = RawBaselineResource(rawId)
            return this
        }

        fun detect(type: StrictPolicyViolation.Type): Request {
            detectionMask = detectionMask or type.mask
            return this
        }

        fun build(): StrictCanary {
            if (detectionMask == 0) {
                throw IllegalStateException("You should detect something")
            }

            return StrictCanary(this)
        }

    }

}