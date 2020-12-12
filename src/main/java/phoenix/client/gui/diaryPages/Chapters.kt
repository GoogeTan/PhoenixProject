package phoenix.client.gui.diaryPages

enum class Chapters(var idIn: Int)
{
    FIRST_DAY(0)
    {
        override fun getText() = "И вот я решил писать дневник. Теперь здесь будут записи о ключевых событиях в моей жизни, заметки и идеи." +
                    "Ну что еще тут написать? Пока все."

        override fun getType() = ChapterType.THOUGHT
    },
    STEEL(1)
    {
        override fun getText() = "Интересно, железо достаточно хрупко, но что если выплавлять его вместе с другими " +
                "материалами? Для этого мне понадобится тара... Думаю горшок из глины подойдет."
        override fun getType() = ChapterType.THOUGHT
    },
    OVEN(2)
    {
        override fun getText() = "Как оказалось, использовать обычную глину не выйдет. Мне нужна более чистая." +
                "Чтоб ее отчистить мне пригодится бочка и вода. А вот из нее буду делать горшок. Назову то тигелем."
        override fun getType() = ChapterType.THOUGHT
    };
    val id = idIn
    abstract fun getText() : String
    abstract fun getType() : ChapterType
}

enum class ChapterType
{
    THOUGHT, GLOBAL;
}