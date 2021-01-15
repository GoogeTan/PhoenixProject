package phoenix.client.gui.diaryPages

import phoenix.client.gui.diaryPages.elements.ADiaryElement
import java.util.*
import kotlin.collections.HashMap

val idToChapter : HashMap<Int, Chapters> = HashMap()

enum class Chapters(var id: Int, var type : ChapterType)
{
    FIRST_DAY(0, ChapterType.THOUGHT)
    {
        override fun getText() =
            "И вот я решил писать дневник. Теперь здесь будут записи о ключевых событиях в моей жизни, заметки и идеи." +
                    "Ну что еще тут написать? Пока все."
    },
    STEEL(1, ChapterType.THOUGHT)
    {
        override fun getText() = "Интересно, железо достаточно хрупко, но что если выплавлять его вместе с другими " +
                "материалами? Для этого мне понадобится тара... Думаю горшок из качественной глины подойдет."
    },
    OVEN(2, ChapterType.THOUGHT)
    {
        override fun getText() = "Как оказалось, использовать обычную глину не выйдет. Мне нужна более чистая и однородная." +
                "Чтоб ее отчистить мне пригодится бочка и вода. А вот из нее буду делать горшок. Назову то тигелем."

    };

    abstract fun getText(): String

    fun getElements(): LinkedList<ADiaryElement>
    {
        val res = LinkedList<ADiaryElement>()



        return res
    }

    init
    {
        idToChapter[id] = this
    }
}

enum class ChapterType
{
    THOUGHT, CHAPTER;
}