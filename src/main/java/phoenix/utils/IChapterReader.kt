package phoenix.utils

interface IChapterReader
{
    fun getOpenedChapters() : List<Pair<Int, Date>>
    fun addChapter(id : Int, date: Date) : Boolean
}
data class Date(var minute : Int, var day : Int, var year: Int)
{
    override fun toString(): String
    {
        return "$minute+*+$day-&-$year"
    }
}