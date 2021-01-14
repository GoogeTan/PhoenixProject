package phoenix.utils

import java.util.ArrayList

interface IChapterReader
{
    fun getOpenedChapters() : ArrayList<Pair<Integer, Date>>
    fun addChapter(id : Int, date: Date) : Boolean
}
data class Date(var minute: Long, var day: Long, var year: Long)
{
    override fun toString(): String
    {
        return "$minute+*+$day-&-$year"
    }
}