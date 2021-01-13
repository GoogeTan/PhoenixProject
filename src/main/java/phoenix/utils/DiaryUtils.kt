package phoenix.utils

import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.RenderComponentsUtil
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import org.apache.commons.lang3.tuple.Pair
import phoenix.client.gui.diaryPages.Chapters
import phoenix.client.gui.diaryPages.elements.ADiaryElement
import phoenix.client.gui.diaryPages.elements.ImageElement
import phoenix.client.gui.diaryPages.elements.RightAlignedTextElement
import phoenix.client.gui.diaryPages.elements.TextElement
import phoenix.utils.exeptions.BookException
import java.lang.StringBuilder
import java.util.ArrayList


object DiaryUtils
{
    //принимает ключи параграфоф
    fun makeParagraphFromTranslate(xSizeIn: Int, font: FontRenderer, vararg keys: String?): ArrayList<ADiaryElement>
    {
        return makeParagraph(font, xSizeIn, StringUtils.translateAll(*keys))
    }

    fun makeParagraph(font: FontRenderer, xSize: Int, text: ArrayList<String?>): ArrayList<ADiaryElement>
    {
        return makeParagraph(font, xSize, *text.toTypedArray())
    }

    fun makeParagraph(font: FontRenderer, xSize: Int, vararg text: String?): ArrayList<ADiaryElement>
    {
        val res = ArrayList<ADiaryElement>()
        val words: MutableList<String> = ArrayList()
        for (current in text)  //проходим по всем параграфам
        {
            if (current != null)
            {
                words.addAll(StringUtils.stringToWords(current))
                //words.addAll(ImmutableList.copyOf(current.split(" ")));
                words.add("[break]")
            }
        }
        var number_of_words = 0
        while (number_of_words < words.size)
        {
            var string_to_add = "" //строка которую будем добавлять
            var next_word = words[number_of_words]
            while (font.getStringWidth("$string_to_add $next_word") < xSize / 2 - 30) //пока меньше ширины страницы
            {
                if (words[number_of_words] == "\\n" || words[number_of_words] == "[break]")
                {
                    res.add(TextElement(string_to_add))
                    string_to_add = ""
                } else
                {
                    string_to_add += "$next_word " //добавляем слово
                }
                ++number_of_words
                next_word = if (number_of_words < words.size) words[number_of_words] else break
            }
            res.add(TextElement(string_to_add)) //добавляем строку
        }
        res.add(TextElement("")) //после каждого параграфа перенос
        return res
    }


    /*
    fun toElements(font: FontRenderer, xSize: Int, vararg text: String): ArrayList<ADiaryElement>
    {
        val words: MutableList<String> = ArrayList()
        for (current in text)  //проходим по всем параграфам
        {
            words.addAll(StringUtils.stringToWords(current))
            //words.addAll(ImmutableList.copyOf(current.split(" ")));
            words.add("[break]")
        }
        val res = ArrayList<ADiaryElement>()
        var tmp = StringBuilder()
        for (s in words)
        {
            if (s != "[break]" && s != "\\n")
            {
                tmp.append(" ").append(s)
            } else
            {
                val components =
                    RenderComponentsUtil.splitText(StringTextComponent(tmp.toString()), xSize, font, false, true)
                for (component in components) res.add(TextElement(component))
                tmp = StringBuilder()
            }
        }
        return res
    }

     */

    @JvmStatic
    fun toElements(font: FontRenderer, xSize: Int, text: Array<out Chapters>): ArrayList<ADiaryElement>
    {
        val words: MutableList<String> = ArrayList()
        for (current in text)  //проходим по всем параграфам
        {
            words.addAll(StringUtils.stringToWords(current.getText()))
            //words.addAll(ImmutableList.copyOf(current.split(" ")));
            words.add("[break]")
        }
        val res = ArrayList<ADiaryElement>()
        var tmp = StringBuilder()
        for (s in words)
        {
            if (s != "[break]" && s != "\\n")
            {
                tmp.append(" ").append(s)
            } else
            {
                val components =
                    RenderComponentsUtil.splitText(StringTextComponent(tmp.toString()), xSize, font, false, true)
                for (component in components) res.add(TextElement(component))
                tmp = StringBuilder()
            }
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

    fun readImageElement(nbt: CompoundNBT, maxSizeX: Int, maxSizeY: Int): ImageElement
    {
        val d = TextureUtils.getTextureSize(ResourceLocation(nbt.getString("res")))
        return ImageElement(ResourceLocation(nbt.getString("res")), d.key, d.value)
    }

    fun read(nbt: CompoundNBT): ADiaryElement?
    {
        return try
        {
            val type = nbt.getString("type")
            when (type)
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
                    ImageElement(ResourceLocation(res), maxSizeX, maxSizeY)
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
