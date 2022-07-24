package st235.com.github.strictcanary.sampleapp

import android.app.Application
import java.io.File
import st235.com.github.strictcanary.StrictCanary
import st235.com.github.strictcanary.data.StrictPolicyViolation

class StrictCanaryApplication: Application() {

    private lateinit var strictCanary: StrictCanary

    override fun onCreate() {
        super.onCreate()

        strictCanary = StrictCanary.Request(this)
            .detect(StrictPolicyViolation.Type.DISK_READ)
            .detect(StrictPolicyViolation.Type.DISK_WRITE)
            .build()

        val file = File(externalCacheDir, "world.txt")
        file.createNewFile()
    }
}