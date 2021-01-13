package phoenix.client.gui.diaryPages.elements

import com.google.common.collect.ImmutableList
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import phoenix.client.gui.diaryPages.Chapters
import phoenix.utils.ArrayUtils
import phoenix.utils.DiaryUtils.toElements
import java.util.*


class DiaryChapter
{
    var font: FontRenderer
    var xSize: Int
    var ySize: Int
    private val elements = LinkedList<ADiaryElement>()
    private var start = 0
    private var end = 0
    private var end2 = 0

    constructor(xSizeIn: Int, ySizeIn: Int)
    {
        font = Minecraft.getInstance().fontRenderer
        xSize = xSizeIn
        ySize = ySizeIn
    }

    constructor(xSizeIn: Int, ySizeIn: Int, vararg elementsIn: ADiaryElement) : this(
        xSizeIn,
        ySizeIn,
        ImmutableList.copyOf<ADiaryElement>(elementsIn)
    )
    {
    }

    constructor(xSizeIn: Int, ySizeIn: Int, vararg elementsIn: Chapters) : this(
        xSizeIn,
        ySizeIn,
        toElements(Minecraft.getInstance().fontRenderer, xSizeIn, elementsIn)
    )
    {
    }

    constructor(xSizeIn: Int, ySizeIn: Int, elementsIn: Collection<ADiaryElement>?)
    {
        font = Minecraft.getInstance().fontRenderer
        xSize = xSizeIn
        ySize = ySizeIn
        add(elementsIn)
    }

    fun add(elementsIn: Collection<ADiaryElement>?)
    {
        elements.addAll(elementsIn!!)
        recalculateSizes()
    }

    private fun recalculateSizes()
    {
        var size = 0
        var count = 0
        for (element in elements)
        {
            if ((size + element.getHeight(xSize, ySize)) * (font.FONT_HEIGHT + 2) >= ySize - 30)
            {
                break
            } else
            {
                size += element.getHeight(xSize, ySize)
                count++
            }
        }
        start = 0
        end = count
        size = 0
        count = 0
        for (i in end until elements.size)
        {
            if ((size + elements[i].getHeight(xSize, ySize)) * font.FONT_HEIGHT >= ySize - 30)
            {
                break
            } else
            {
                size += elements[i].getHeight(xSize, ySize)
                count++
            }
        }
        end2 = end + count + 1
    }

    fun toFirst()
    {
        start = 0
        end = 0
        end2 = 0
        recalculateSizes()
    }

    val isLast: Boolean
        get()
        = end >= elements.size - 1 || end2 >= elements.size - 1
    val isFirst: Boolean
        get()
        = start <= 0
    val currentPage1: ArrayList<ADiaryElement>
        get()
        = ArrayUtils.part(elements, start, end)
    val currentPage2: ArrayList<ADiaryElement>
        get()
        = ArrayUtils.part(elements, end, end2)

    operator fun next()
    {
        println(elements.size)
        if (!isLast)
        {
            var size = 0
            var count = 0
            var isEnded = false
            run {
                var i = end2
                while (i < elements.size && !isEnded)
                {
                    if ((size + elements[i].getHeight(xSize, ySize)) * font.FONT_HEIGHT >= ySize - 30)
                    {
                        isEnded = true
                    }
                    size += elements[i].getHeight(xSize, ySize)
                    count++
                    i++
                }
            }
            start = end2 + 1
            end = end2 + count + 1
            size = 0
            count = 0
            isEnded = false
            var i = end
            while (i < elements.size && !isEnded)
            {
                if (size + elements[i].getHeight(xSize, ySize) >= 14)
                {
                    isEnded = true
                }
                size += elements[i].getHeight(xSize, ySize)
                count++
                i++
            }
            end2 = end + count + 1
        }
    }

    fun prev()
    {
        println(elements.size)
        if (!isFirst)
        {
            var size = 0
            var count = 0
            var isEnded = false
            run {
                var i = start - 1
                while (i >= 0 && !isEnded)
                {
                    if ((size + elements[i].getHeight(xSize, ySize)) * font.FONT_HEIGHT >= ySize - 30)
                    {
                        isEnded = true
                    } else
                    {
                        size += elements[i].getHeight(xSize, ySize)
                        count++
                    }
                    --i
                }
            }
            end -= count + 1
            end2 -= count + 1
            size = 0
            count = 0
            isEnded = false
            var i = end - 1
            while (i >= 0 && !isEnded)
            {
                if ((size + elements[i].getHeight(xSize, ySize)) * font.FONT_HEIGHT >= ySize - 30)
                {
                    isEnded = true
                } else
                {
                    size += elements[i].getHeight(xSize, ySize)
                    count++
                }
                --i
            }
            start = end - count - 1
        }
    }

    override fun toString(): String
    {
        return elements.toString()
    }
}
