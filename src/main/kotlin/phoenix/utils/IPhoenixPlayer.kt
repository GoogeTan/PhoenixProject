package phoenix.utils

import phoenix.client.gui.diaryPages.Chapters
import java.util.*

interface IPhoenixPlayer
{
    fun getOpenedChapters() : ArrayList<Pair<Int, Date>>
    //Use ServerPlayerEntity.addChapter(Chapters)
    fun addChapter(id : Int, date: Date) : Boolean
    fun hasChapter(id: Int) : Boolean
    fun hasChapter(ch : Chapters) : Boolean = hasChapter(ch.id)
    fun isOnCauda() : Boolean
}

data class Date(var minute: Long, var day: Long, var year: Long)
{
    override fun toString() = "$minute+*+$day-&-$year"
}