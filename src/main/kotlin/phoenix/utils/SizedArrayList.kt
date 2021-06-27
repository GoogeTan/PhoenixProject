package phoenix.utils

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

    fun resize(newSize : Int, t : T)
    {
        if(newSize < 0 || newSize == size)
            return
        if(newSize > size)
            for (i in size..newSize)
                add(t)
        else
        {
            for (i in size..newSize)
                removeAt(i)
        }
    }

    fun resize(newSize : Int, initializer : (index : Int) -> T)
    {
        if(newSize < 0 || newSize == size)
            return
        if(newSize > size)
            for (i in size..newSize)
                add(initializer(i))
        else
        {
            for (i in size..newSize)
                removeAt(i)
        }
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
            } else
            {
                SizedArrayList()
            }
        }

        fun<T> copyOf(tin : Array<T>) : SizedArrayList<T> = if(tin.isNotEmpty()) SizedArrayList(tin.size, tin::get) else SizedArrayList()

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

        fun<T> copyOf(tin : List<T>) : SizedArrayList<T> =  if (tin.isNotEmpty()) SizedArrayList(tin.size, tin::get) else SizedArrayList()
    }
}