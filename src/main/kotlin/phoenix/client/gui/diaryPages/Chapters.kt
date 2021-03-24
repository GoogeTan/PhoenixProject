package phoenix.client.gui.diaryPages

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
                "материалами? Но для этого мне понадобится тара... Думаю горшок из качественной глины подойдет."
    },
    CLAY(2, ChapterType.THOUGHT)
    {
        override fun getText() = "Как оказалось, использовать обычную глину не выйдет. Результат годится только цветы растить. " +
                "Нужна более чистая и однородная глина... Чтоб ее отчистить мне пригодится бочка и вода. А вот из нее буду делать " +
                "горшок. Назову это тигелем."
    },
    OVEN(3, ChapterType.THOUGHT)
    {
        override fun getText() = "В обыкновенной печи не хватит температур, для взаимодействия железа и угля... " +
                "Нужно придумать печку потеплее"
    };

    abstract fun getText(): String

    init
    {
        idToChapter[id] = this
    }
}

enum class ChapterType
{
    THOUGHT, CHAPTER;
}