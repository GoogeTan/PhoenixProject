package phoenix.other.collections

class SizedArrayList<T> : ArrayList<T>
{
    constructor() : super()
    constructor(size: Int) : super(size)
    constructor(size : Int, t : T) : super(size)
    {
        for (i in 0 until size)
            add(t)
    }

    constructor(size : Int, initializer : (index : Int) -> T) : super(size)
    {
        for (i in 0 until size)
            add(initializer(i))
    }

    fun resize(newSize : Int, initializer : (index : Int) -> T)
    {
        if (newSize < 0)
            throw ArrayIndexOutOfBoundsException(newSize)
        if(newSize == size) return

        if(newSize > size)
        {
            for (i in size until newSize)
            {
                add(initializer(i))
            }
        }
        else
        {
            removeRange(newSize, size)
        }
    }

    override fun equals(other: Any?): Boolean
    {
        if (other == null) return false;
        if (this === other) return true
        if (other is List<*>)
        {
            if (this.size != other.size)
                return false;

            for (i in indices)
                if (this[i] != other[i])
                    return false
        }
        else if (other is Collection<*>)
        {
            if (this.size != other.size)
                return false;
            for ((index, element) in other.withIndex())
                if (element != this[index])
                    return false
            return true
        }
        return true
    }

    companion object
    {
        fun<T> of(vararg elementsIn : T) : SizedArrayList<T>
        {
            return if(elementsIn.isNotEmpty())
            {
                val res = SizedArrayList<T>(elementsIn.size)
                for (t in elementsIn)
                    res.add(t)
                res
            }
            else
            {
                SizedArrayList()
            }
        }

        fun<T> copyOf(tin : Array<T>) : SizedArrayList<T> = if (tin.isNotEmpty()) SizedArrayList(tin.size, tin::get) else SizedArrayList()
        fun<T> copyOf(tin :  List<T>) : SizedArrayList<T> = if (tin.isNotEmpty()) SizedArrayList(tin.size, tin::get) else SizedArrayList()
        fun<T> copyOf(tin : Collection<T>) : SizedArrayList<T>
        {
            return if(tin.isNotEmpty())
            {
                val res = SizedArrayList<T>(tin.size)
                for (t in tin)
                    res.add(t)
                res
            } else
            {
                SizedArrayList()
            }
        }

    }
}