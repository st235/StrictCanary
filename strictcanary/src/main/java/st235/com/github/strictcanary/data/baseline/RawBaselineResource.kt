package st235.com.github.strictcanary.data.baseline

import android.content.Context
import androidx.annotation.RawRes
import java.io.InputStream

class RawBaselineResource(
    @RawRes
    private val rawId: Int
): BaselineResource {

    override fun open(context: Context): InputStream {
        return context.resources.openRawResource(rawId)
    }
}