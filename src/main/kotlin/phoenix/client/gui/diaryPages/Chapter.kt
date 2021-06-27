package phoenix.client.gui.diaryPages

import net.minecraft.client.resources.I18n

val ID_TO_CHAPTER : HashMap<Int, Chapter> = HashMap()

enum class Chapter(var id: Int, var type : ChapterType, val chapterName : String)
{
    FIRST_DAY(0, ChapterType.THOUGHT, "at_start"),
    STEEL(1, ChapterType.THOUGHT, "steel"),
    CLAY(2, ChapterType.THOUGHT, "clay"),
    OVEN(3, ChapterType.THOUGHT, "oven"),
    REDO(4, ChapterType.THOUGHT, "redo"),
    THE_WHO(5, ChapterType.STORY, "the_who");

    fun getText() : String = I18n.format("phoenix.book.$chapterName")

    init
    {
        ID_TO_CHAPTER[id] = this
    }

    enum class ChapterType
    {
        THOUGHT, STORY;
    }
}