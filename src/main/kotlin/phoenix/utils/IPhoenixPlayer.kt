package phoenix.utils

import phoenix.client.gui.diaryPages.Chapter

interface IPhoenixPlayer
{
    fun getOpenedChapters() : ArrayList<Pair<Int, Date>>
    //Use ServerPlayerEntity.addChapter(Chapter)
    fun addChapter(id : Int, date: Date) : Boolean
    fun hasChapter(id: Int) : Boolean
    fun hasChapter(ch : Chapter) : Boolean = hasChapter(ch.id)
}

data class Date(var minute: Long, var day: Long, var year: Long)
{
    override fun toString() = "$minute+*+$day-&-$year"
}