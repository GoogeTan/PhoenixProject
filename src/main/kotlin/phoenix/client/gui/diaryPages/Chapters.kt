package phoenix.client.gui.diaryPages

import net.minecraft.client.resources.I18n

val idToChapter : HashMap<Int, Chapters> = HashMap()

enum class Chapters(var id: Int, var type : ChapterType, val chapterName : String)
{
    FIRST_DAY(0, ChapterType.THOUGHT, "at_start"),
    STEEL(1, ChapterType.THOUGHT, "steel"),
    CLAY(2, ChapterType.THOUGHT, "clay"),
    OVEN(3, ChapterType.THOUGHT, "oven"),
    REDO(4, ChapterType.THOUGHT, "redo");

    fun getText() : String = I18n.format("phoenix.book.$chapterName")

    init
    {
        idToChapter[id] = this
    }

    enum class ChapterType
    {
        THOUGHT, LIST;
    }
}