package phoenix.client.gui.diary.elements

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.FontRenderer
import phoenix.client.gui.DiaryGui

class DiaryPage(private val maxSize : Int, val elements : ArrayList<ADiaryElement>)
{
    private var sum = 0
    constructor(maxSize : Int) : this(maxSize, ArrayList())

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

    fun render(gui: DiaryGui, font: FontRenderer, xSize: Int, ySize: Int, depth: Int)
    {
        RenderSystem.pushMatrix()
        var sum = 0
        for (element in elements)
        {
            RenderSystem.translatef(0f, (font.FONT_HEIGHT + 2).toFloat(), 0f)
            element.render(gui, font, xSize, ySize, 0, 0, depth)
            sum += element.getHeight(xSize, ySize)
        }
        RenderSystem.popMatrix()
    }

    override fun toString(): String = "DiaryPage(elements=$elements)"
}