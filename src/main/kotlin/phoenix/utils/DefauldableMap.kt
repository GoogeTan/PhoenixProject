package phoenix.utils

class DefauldableMap<K, V>(val defaulter: (key: K) -> V) : HashMap<K, V>()
{
    override fun get(key: K): V
    {
        return super.getOrDefault(key, defaulter(key))
    }
}