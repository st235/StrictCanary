package st235.com.github.strictcanary.sampleapp

import android.app.Application
import java.io.File
import st235.com.github.strictcanary.StrictCanary
import st235.com.github.strictcanary.StrictCanaryDetectionPolicy
import st235.com.github.strictcanary.data.StrictCanaryViolation

class StrictCanaryApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        StrictCanary.setDetectionPolicy(
            StrictCanaryDetectionPolicy.Builder(this)
                .detect(StrictCanaryViolation.Type.DISK_READ)
                .detect(StrictCanaryViolation.Type.DISK_WRITE)
                .build()
        )

        val file = File(externalCacheDir, "world.txt")
        file.createNewFile()
    }
}