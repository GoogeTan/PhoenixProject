package phoenix.client.gui.diaryPages.elements

import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import phoenix.containers.DiaryContainer

class DiaryPage(val elements : ArrayList<ADiaryElement>)
{
    constructor() : this(ArrayList())
    fun isFull(sizeX : Int, sizeY : Int) : Boolean
    {
        var sum = 0;
        for (el in elements)
            sum += el.getHeight(sizeX, sizeY)

        return sum < 14
    }


    fun tryAdd(element: ADiaryElement, sizeX: Int, sizeY: Double) : Boolean
    {
        var sum = 0;
        for (el in elements)
            sum += el.getHeight(sizeX, sizeY.toInt())

        return if (sum + element.getHeight(sizeX, sizeY.toInt()) < 14)
        {
            elements.add(element)
            true
        } else
        {
            false
        }
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
}