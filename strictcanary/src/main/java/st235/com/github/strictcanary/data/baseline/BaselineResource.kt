package st235.com.github.strictcanary.data.baseline

import android.content.Context
import androidx.annotation.WorkerThread
import java.io.InputStream

interface BaselineResource {

    @WorkerThread
    fun open(context: Context): InputStream

}