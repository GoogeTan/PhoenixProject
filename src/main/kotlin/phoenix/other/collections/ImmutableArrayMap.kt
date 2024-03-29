package phoenix.other.collections

import com.google.common.collect.ImmutableSet
import phoenix.other.*

class ImmutableArrayMap<K : Comparable<K>, out V> private constructor(source: ArrayList<Pair<K, V>>) : Map<K, V>, Iterable<Pair<K, V>>
{
    val keysList: List<K>
    val valuesList : List<V>
    override val size: Int get() = keysList.size
    override val entries: Set<Map.Entry<K, V>> = ImmutableSet.copyOf(foreach(ArrayList<Map.Entry<K, V>>()) { value -> add(ImmutableArrayEntry(value.first, value.second)) })
    override val keys  : Set<K> get() = ImmutableSet.copyOf(keysList)
    override val values: Collection<V> get() = ImmutableSet.copyOf(valuesList)

    init
    {
        source.sortWith(Comparator { pair, pair2 -> pair.first.compareTo(pair2.first) })
        keysList = List(source.size) { source[it].first }
        valuesList = List(source.size) { source[it].second }
    }

    constructor(source: Collection<Pair<K, V>>) : this(source.toMutableList().sortedBy(comparePairByFirst()).unique { p1, p2 -> p1.first != p2.first })
    constructor(source: Array<Pair<K, V>>)      : this(source.toMutableList().sortedBy(comparePairByFirst()).unique { p1, p2 -> p1.first != p2.first })

    override fun containsKey(key: K): Boolean = getIndex(key) != null
    override fun containsValue(value: @UnsafeVariance V): Boolean = valuesList.contains(value)
    override fun isEmpty(): Boolean = keysList.isEmpty()

    override operator fun get(key: K): V? = getIndex(key).ifNotNull { valuesList[this] }

    private fun getIndex(key : K) : Int?
    {
        var left = 0
        var right = keysList.size - 1
        while (right - left > 1)
        {
            val middle = (left + right) / 2
            val element = keysList[middle]
            when
            {
                element < key -> left = middle
                element > key -> right = middle
                else          -> return middle
            }
        }
        return null
    }

    inner class ImmutableArrayEntry(override val key: K, override val value: V) : Map.Entry<K, V>

    override operator fun iterator(): Iterator<Pair<K, V>> = object : Iterator<Pair<K, V>>
    {
        var index = 0
        override fun hasNext(): Boolean = index + 1 < size
        override fun next(): Pair<K, V> = Pair(keysList[index], valuesList[index++])
    }

    override fun toString(): String
    {
        val builder = StringBuilder("Map[")
        for (i in this)
            builder.append("$i, ")
        builder.append("]")
        return super.toString()
    }

    class Builder<Key : Comparable<Key>, Value>
    {
        private val collection: MutableList<Pair<Key, Value>> = ArrayList()

        constructor()

        constructor(key1: Key, value1: Value)
        {
            put(key1, value1)
        }

        constructor(
            key1: Key,
            value1: Value,
            key2: Key,
            value2: Value
        )
        {
            put(key1, value1)
            put(key2, value2)
        }

        constructor(
            key1: Key,
            value1: Value,
            key2: Key,
            value2: Value,
            key3: Key,
            value3: Value
        )
        {
            put(key1, value1)
            put(key2, value2)
            put(key3, value3)
        }

        constructor(
            key1: Key,
            value1: Value,
            key2: Key,
            value2: Value,
            key3: Key,
            value3: Value,
            key4: Key,
            value4: Value
        )
        {
            put(key1, value1)
            put(key2, value2)
            put(key3, value3)
        }

        fun put(key: Key, value: Value): Builder<Key, Value>
        {
            collection.add(Pair(key, value))
            return this
        }

        fun build() = ImmutableArrayMap(collection)
    }
}