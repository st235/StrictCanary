package st235.com.github.strictcanary.data.baseline

import android.content.Context
import java.io.InputStream

class AssetsBaselineResource(
    private val filePath: String
): BaselineResource {

    override fun open(context: Context): InputStream {
        return context.assets.open(filePath)
    }
}