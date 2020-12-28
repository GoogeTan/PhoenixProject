package phoenix.utils

class SizedArrayList<T> : ArrayList<T>
{
    constructor() : super()
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
}