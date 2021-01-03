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

    fun resize(newSize : Int, t : T)
    {
        if(newSize < 0 || newSize == size)
            return;
        if(newSize > size)
            for (i in size..newSize)
                add(t)
        else
        {
            for (i in size..newSize)
                removeAt(i)
        }
    }
    companion object
    {
        @JvmStatic
        fun<T> of(vararg tin : T) : SizedArrayList<T>
        {
            return if(tin.size > 0)
            {
                val res = SizedArrayList<T>(tin.size)
                for (t in tin)
                    res.add(t)
                res;
            } else
            {
                SizedArrayList()
            }
        }

        @JvmStatic
        fun<T> copyOf(tin : Array<T>) : SizedArrayList<T>
        {
            return if(tin.size > 0)
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

        @JvmStatic
        fun<T> copyOf(tin : List<T>) : SizedArrayList<T>
        {
            return if(tin.size > 0)
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

        @JvmStatic
        fun<T> copyOf(tin : Set<T>) : SizedArrayList<T>
        {
            return if(tin.size > 0)
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