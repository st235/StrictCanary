package st235.com.github.strictcanary.data.baseline

import android.content.Context
import java.io.InputStream

interface BaselineResource {

    fun open(context: Context): InputStream

}