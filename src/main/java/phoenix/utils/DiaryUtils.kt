package phoenix.utils

import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.RenderComponentsUtil
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.StringTextComponent
import org.apache.commons.lang3.tuple.Pair
import phoenix.client.gui.diaryPages.Chapters
import phoenix.client.gui.diaryPages.elements.*
import java.util.*

object DiaryUtils
{
    //принимает ключи параграфов
    fun makeParagraphFromTranslate(xSizeIn: Int, font: FontRenderer, vararg keys: String): ArrayList<ADiaryElement>
    {
        return makeParagraph(font, xSizeIn, StringUtils.translateAll(*keys))
    }

    fun makeParagraph(font: FontRenderer, xSize: Int, text: ArrayList<String>): ArrayList<ADiaryElement> = makeParagraph(font, xSize, *text.toTypedArray())

    fun makeParagraph(font: FontRenderer, xSize: Int, vararg text: String): ArrayList<ADiaryElement>
    {
        val res = ArrayList<ADiaryElement>()
        val words: MutableList<String> = ArrayList()
        for (current in text)  //проходим по всем параграфам
        {
            words.addAll(StringUtils.stringToWords(current))
            //words.addAll(ImmutableList.copyOf(current.split(" ")));
            words.add("[break]")
        }
        var numberOfWords = 0
        while (numberOfWords < words.size)
        {
            var stringToAdd = "" //строка которую будем добавлять
            var nextWord = words[numberOfWords]
            while (font.getStringWidth("$stringToAdd $nextWord") < xSize - 30) //пока меньше ширины страницы
            {
                if (words[numberOfWords] == "\\n" || words[numberOfWords] == "[break]")
                {
                    if(stringToAdd.isNotEmpty())
                        res.add(TextElement(stringToAdd))
                    stringToAdd = ""
                } else
                {
                    stringToAdd += "$nextWord " //добавляем слово
                }
                ++numberOfWords
                nextWord = if (numberOfWords < words.size) words[numberOfWords] else break
            }
            if(stringToAdd.isNotEmpty())
                res.add(TextElement(stringToAdd)) //добавляем строку
        }
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


    @JvmStatic
    fun pagesFrom(font: FontRenderer, xSize: Int, ySize: Int, text: Array<out Chapters>): ArrayList<DiaryPage>
    {
        val elements = toElements(font, xSize, text)
        val res = ArrayList<DiaryPage>()
        var currentPage = DiaryPage()
        var i = 0
        while (i in 0 until elements.size)
        {
            while (i in 0 until elements.size && currentPage.tryAdd(elements[i], xSize, ySize.toDouble()))
            {
                i++
            }
            res.add(currentPage)
            currentPage = DiaryPage()
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
        return ImageElement(TextureLocation(nbt.getString("res")), d.key, d.value)
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
