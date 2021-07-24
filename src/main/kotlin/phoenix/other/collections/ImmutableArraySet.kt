package phoenix.other.collections

import phoenix.other.makeVariableInConstructor
import phoenix.other.unique

class ImmutableArraySet<out V : Comparable<@kotlin.UnsafeVariance V>> private constructor(val elements: List<V>) : Set<V>, Iterable<V>
{
    override val size: Int = elements.size
    private constructor(source: ArrayList<V>) : this(List(source.size,  source.unique()::get))
    constructor(source : Collection<V>) : this(ArrayList<V>().apply { addAll(source) }.unique())
    constructor(source : Array<V>     ) : this(ArrayList<V>().apply { addAll(source) }.unique())

    override fun contains(element: @UnsafeVariance V): Boolean
    {
        var left = 0
        var right = elements.size - 1
        while (right - left > 1)
        {
            val middle = (left + right) / 2
            val elem = elements[middle]
            when
            {
                elem < element -> left = middle
                elem > element -> right = middle
                else          -> return true
            }
        }
        return false
    }

    override fun isEmpty(): Boolean = elements.isEmpty()
    override operator fun iterator(): Iterator<V> = elements.iterator()

    override fun containsAll(elements: Collection<@UnsafeVariance V>): Boolean
    {
        for (value in elements)
            if (!contains(value))
                return false
        return true
    }
}