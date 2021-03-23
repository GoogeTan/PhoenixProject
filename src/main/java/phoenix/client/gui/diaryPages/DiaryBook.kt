package phoenix.client.gui.diaryPages

import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import phoenix.client.gui.diaryPages.elements.ADiaryElement
import phoenix.client.gui.diaryPages.elements.DiaryPage
import phoenix.containers.DiaryContainer
import java.util.*

class DiaryBook(private val xSize: Int, private val ySize: Int, private val font: FontRenderer)
{
    private val pages = LinkedList<DiaryPage>()
    private var page = 0

    fun add(elements: kotlin.collections.ArrayList<ADiaryElement>)
    {
        var i = 0
        while (i in 0 until elements.size)
        {
            if(pages.size == 0)
                pages.add(DiaryPage())
            while (i < elements.size && pages.last.tryAdd(elements[i], xSize, ySize))
            {
                i++
            }
            pages.add(DiaryPage())
        }
    }

    fun add(element: ADiaryElement) = add(arrayListOf(element))

    fun render(gui: ContainerScreen<DiaryContainer>, renderer: FontRenderer, xSize: Int, ySize: Int, x: Int, y: Int, depth: Int)
    {
        leftPage.render(gui, font, xSize / 2, ySize, x, y, depth)
        rightPage.render(gui, renderer, xSize / 2, ySize, x + xSize / 2, y, depth)
    }

    private val isLast : Boolean get() = page + 1 >= pages.size - 1
    private val isFirst: Boolean get() = page     <= 0
    private val leftPage: DiaryPage  get() = if(page     in 0 until pages.size) pages[page]     else DiaryPage()
    private val rightPage: DiaryPage get() = if(page + 1 in 0 until pages.size) pages[page + 1] else DiaryPage()

    fun next() = if (!isLast)  page += 2 else page += 0
    fun prev() = if (!isFirst) page -= 2 else page += 0
}