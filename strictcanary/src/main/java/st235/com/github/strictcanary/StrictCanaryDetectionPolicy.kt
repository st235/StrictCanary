package st235.com.github.strictcanary

import android.content.Context
import androidx.annotation.RawRes
import st235.com.github.strictcanary.data.StrictCanaryViolation
import st235.com.github.strictcanary.data.baseline.AssetsBaselineResource
import st235.com.github.strictcanary.data.baseline.BaselineDescriptor
import st235.com.github.strictcanary.data.baseline.BaselineFormat
import st235.com.github.strictcanary.data.baseline.RawBaselineResource
import st235.com.github.strictcanary.data.detection.DetectionDescriptor
import st235.com.github.strictcanary.utils.notifications.NotificationManager

data class StrictCanaryDetectionPolicy internal constructor(
    internal val context: Context,
    internal val notificationStrategy: NotificationManager.Strategy,
    internal val detectionDescriptor: DetectionDescriptor,
    internal val baselineDescriptor: BaselineDescriptor?
) {

    class Builder(
        internal val context: Context
    ) {

        private var detectionMask: Int = 0
        private var shouldSkipThirdPartyLibraries: Boolean = false
        private var notificationStrategy = NotificationManager.Strategy.ALL_AT_ONCE

        private var baselineDescriptor: BaselineDescriptor? = null

        fun shouldSkipThirdPartyLibraries(shouldSkipThirdPartyLibraries: Boolean): Builder {
            this.shouldSkipThirdPartyLibraries = shouldSkipThirdPartyLibraries
            return this
        }

        fun assetsBaseline(path: String, baselineFormat: BaselineFormat = BaselineFormat.XML): Builder {
            this.baselineDescriptor = BaselineDescriptor(
                resource = AssetsBaselineResource(path),
                format = baselineFormat
            )
            return this
        }

        fun rawBaseline(@RawRes rawId: Int, baselineFormat: BaselineFormat = BaselineFormat.XML): Builder {
            this.baselineDescriptor = BaselineDescriptor(
                resource = RawBaselineResource(rawId),
                format = baselineFormat
            )
            return this
        }

        fun showEveryViolationAsSeparateNotification(): Builder {
            this.notificationStrategy = NotificationManager.Strategy.EVERY_VIOLATION
            return this
        }

        fun showAllViolationsAtOnce(): Builder {
            this.notificationStrategy = NotificationManager.Strategy.ALL_AT_ONCE
            return this
        }

        fun detect(type: StrictCanaryViolation.Type): Builder {
            detectionMask = detectionMask or type.mask
            return this
        }

        fun build(): StrictCanaryDetectionPolicy {
            if (detectionMask == 0) {
                throw IllegalStateException("You should detect something")
            }

            return StrictCanaryDetectionPolicy(
                context = this.context,
                notificationStrategy = this.notificationStrategy,
                detectionDescriptor = DetectionDescriptor(
                    mask = detectionMask,
                    shouldSkipThirdPartyLibraries = this.shouldSkipThirdPartyLibraries
                ),
                baselineDescriptor = this.baselineDescriptor
            )
        }

    }

}
