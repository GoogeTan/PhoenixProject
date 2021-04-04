package phoenix.utils

import net.minecraft.client.gui.FontRenderer
import net.minecraft.nbt.CompoundNBT
import org.apache.commons.lang3.tuple.Pair
import phoenix.client.gui.diaryPages.elements.ADiaryElement
import phoenix.client.gui.diaryPages.elements.ImageElement
import phoenix.client.gui.diaryPages.elements.RightAlignedTextElement
import phoenix.client.gui.diaryPages.elements.TextElement
import phoenix.utils.StringUtils.toWords
import java.util.*

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

    fun add(chapter: ArrayList<ADiaryElement>, vararg toAdd: Pair<Int, ADiaryElement>): ArrayList<ADiaryElement>
    {
        for (pair in toAdd)
        {
            if (chapter.size < pair.left)
            {
                chapter.add(pair.right)
            } else
            {
                chapter.add(pair.left, pair.right)
            }
        }
        return chapter
    }

    fun read(nbt: CompoundNBT): ADiaryElement?
    {
        return try
        {
            when (nbt.getString("type"))
            {
                "text"  ->
                {
                    val text = nbt.getString("text")
                    TextElement(text)
                }
                "rtext" ->
                {
                    val text = nbt.getString("text")
                    RightAlignedTextElement(text)
                }
                "img"   ->
                {
                    val res = nbt.getString("res")
                    val maxSizeX = nbt.getInt("maxx")
                    val maxSizeY = nbt.getInt("maxy")
                    ImageElement(TextureLocation(res), maxSizeX, maxSizeY)
                }
                else    ->
                {
                    null
                }
            }
        } catch (e: Exception)
        {
            throw BookException("Can not read element")
        }
    }
}
