package phoenix.utils

import net.minecraft.client.gui.FontRenderer
import phoenix.client.gui.diary.elements.ADiaryElement
import phoenix.client.gui.diary.elements.TextElement
import phoenix.utils.StringUtils.toWords

object DiaryUtils
{
    fun makeParagraph(font: FontRenderer, xSize: Int, vararg text: String): ArrayList<ADiaryElement>
    {
        val res = ArrayList<ADiaryElement>()
        val words: MutableList<String> = ArrayList()
        for (current in text)  //проходим по всем параграфам
        {
            words.addAll(current.toWords())
            words.add("[n]")
        }

        var i = 0
        while (i < words.size)
        {
            var stringToAdd = ""
            var nextWord = words[i]

            while (i < words.size && font.getStringWidth("$stringToAdd $nextWord") < xSize * 1.8)
            {
                stringToAdd += " $nextWord"
                i++
                if (i < words.size)
                    nextWord = words[i]
            }

            if(stringToAdd.isNotEmpty())
                res.add(TextElement(stringToAdd))
        }

        return res
    }
}
