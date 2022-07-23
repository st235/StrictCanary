package st235.com.github.strictcanary.data.utils

import android.content.Context
import java.io.InputStream
import st235.com.github.strictcanary.data.baseline.BaselineResource

class TestBaselineResource(
    private val path: String
): BaselineResource {

    override fun open(context: Context): InputStream {
        return ResourcesHelper.read(path)
    }
}