package st235.com.github.strictcanary.utils

import java.util.LinkedList

internal class LruHashSet<T>(
    private val maxSize: Long
): Iterable<T> {

    private var size: Long = 0L

    private val set = mutableSetOf<T>()
    private val list = LinkedList<T>()

    @get:Synchronized
    val snapshot: List<T>
    get() {
        return ArrayList(list)
    }

    @Synchronized
    fun add(item: T) {
        if (set.contains(item)) {
            return
        }

        list.add(item)
        set.add(item)
        evictIfNeeded()
    }

    @Synchronized
    override fun iterator(): Iterator<T> {
        return list.iterator()
    }

    @Synchronized
    private fun evictIfNeeded() {
        while (!list.isEmpty() && size > maxSize) {
            val item = list.remove()
            set.remove(item)
            size -= sizeOf(item)
        }
    }

    @Synchronized
    private fun sizeOf(item: T): Long {
        return 1L
    }

}