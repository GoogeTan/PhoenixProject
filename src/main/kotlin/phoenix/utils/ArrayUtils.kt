package phoenix.utils

import java.util.*


object ArrayUtils
{
    operator fun <T> ArrayList<T>.plus(second: ArrayList<T>): ArrayList<T>
    {
        val res = ArrayList(this)
        res.addAll(second)
        return res
    }

    operator fun <T> ArrayList<T>.get(range: IntRange): ArrayList<T>
    {
        val res = ArrayList<T>()
        for (i in range)
        {
            res.add(this[getIndex(size, i)])
        }
        return res
    }

    fun <T> MutableList<T>.resize(newSize: Int, toFill: T): List<T>
    {
        if (this.size > newSize)
        {
            this.subList(newSize, this.size).clear()
        } else if (this.size < newSize)
        {
            for (i in this.size until newSize) this.add(toFill)
        }
        return this
    }

    fun <T> part(list: LinkedList<T>, from: Int, to: Int): LinkedList<T>
    {
        val res = LinkedList<T>()
        var i = from
        while (i < to && i < list.size)
        {
            res.add(list[getIndex(list.size, i)])
            i++
        }
        return res
    }

    private fun getIndex(size: Int, indexIn: Int): Int
    {
        var index = indexIn
        if (size == 0) return 0
        while (index < 0) index += size
        return if (index >= size) size - 1 else index
    }
}
