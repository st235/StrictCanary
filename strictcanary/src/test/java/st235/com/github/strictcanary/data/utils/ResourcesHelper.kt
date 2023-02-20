package st235.com.github.strictcanary.data.utils

import java.io.InputStream

object ResourcesHelper {

    fun read(path: String): InputStream {
     return javaClass.classLoader?.getResourceAsStream(path) ?:
      throw IllegalStateException("No classloader found")
    }

}