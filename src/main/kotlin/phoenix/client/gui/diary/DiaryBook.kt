package phoenix.client.gui.diary

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.FontRenderer
import phoenix.client.gui.DiaryGui
import phoenix.client.gui.diary.elements.ADiaryElement
import phoenix.client.gui.diary.elements.DiaryPage
import phoenix.other.matrix
import java.util.*

class DiaryBook(private val xSize: Int, private val ySize: Int, private val font: FontRenderer)
{
    private val pages = LinkedList<DiaryPage>()
    private var page = 0

    fun add(elements: List<ADiaryElement>)
    {
        var i = 0
        while (i in elements.indices)
        {
            if(pages.size == 0)
                pages.add(DiaryPage(ySize / font.FONT_HEIGHT))
            while (i < elements.size && pages.last.tryAdd(elements[i], xSize, ySize))
            {
                i++
            }
            pages.add(DiaryPage(ySize / font.FONT_HEIGHT))
        }
    }
    fun add(vararg elements: ADiaryElement)
    {
        var i = 0
        while (i in elements.indices)
        {
            if(pages.size == 0)
                pages.add(DiaryPage(ySize / font.FONT_HEIGHT))
            while (i < elements.size && pages.last.tryAdd(elements[i], xSize, ySize))
            {
                i++
            }
            pages.add(DiaryPage(ySize / font.FONT_HEIGHT))
        }
    }

    fun render(gui: DiaryGui, renderer: FontRenderer, xSize: Int, ySize: Int, depth: Int)
    {
        matrix {
            //scale(0.5f, 0.5f, 1f) {
            leftPage.render(gui, font, xSize / 2, ySize, depth)
            //}
        }
        RenderSystem.translatef(xSize / 2f, 0f, 0f)
        //scale(0.5f, 0.5f, 1f) {
            rightPage.render(gui, renderer, xSize / 2, ySize, depth)
        //}
    }

    private val isLast : Boolean get() = page + 1 >= pages.size - 1
    private val isFirst: Boolean get() = page     <= 0
    private val leftPage : DiaryPage get() = if(page     in 0 until pages.size) pages[page]     else DiaryPage(ySize / font.FONT_HEIGHT)
    private val rightPage: DiaryPage get() = if(page + 1 in 0 until pages.size) pages[page + 1] else DiaryPage(ySize / font.FONT_HEIGHT)

    fun next() = if (!isLast)  page += 2 else page += 0
    fun prev() = if (!isFirst) page -= 2 else page += 0

    override fun toString(): String = "DiaryBook(xSize=$xSize, ySize=$ySize, font=$font, pages=$pages, page=$page)"
}