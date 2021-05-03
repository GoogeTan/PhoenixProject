package phoenix.client.gui.diaryPages.elements

import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import phoenix.containers.DiaryContainer

class DiaryPage(val maxSize : Int, val elements : ArrayList<ADiaryElement>)
{
    var sum = 0
    constructor(maxSize : Int) : this(maxSize, ArrayList())

    fun isFull(sizeX : Int, sizeY : Int) : Boolean = sum < maxSize

    fun tryAdd(element: ADiaryElement, sizeX: Int, sizeY: Int) : Boolean
    {
        return if (sum + element.getHeight(sizeX, sizeY) < maxSize)
        {
            sum += element.getHeight(sizeX, sizeY)
            elements.add(element)
            true
        }
        else false
    }

    fun render(gui: ContainerScreen<DiaryContainer>, font: FontRenderer, xSize: Int, ySize: Int, x: Int, y: Int, depth: Int)
    {
        var sum = 0
        for (element in elements)
        {
            element.render(gui, font, xSize, ySize, x, y + sum * (font.FONT_HEIGHT + 2), depth)
            sum += element.getHeight(xSize, ySize)
        }
    }

    override fun toString(): String = "DiaryPage(elements=$elements)"
}