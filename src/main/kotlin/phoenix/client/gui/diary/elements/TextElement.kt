package phoenix.client.gui.diary.elements

import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.util.text.TextFormatting
import phoenix.containers.DiaryContainer

open class TextElement : ADiaryElement
{
    var text: ITextComponent
    val color : Int

    constructor(text: ITextComponent, color: Int = TextFormatting.BLACK.color!!)
    {
        this.text = text
        this.color = color
    }

    constructor(text: String, color: Int = TextFormatting.BLACK.color!!)
    {
        this.text = StringTextComponent(text)
        this.color = color
    }

    override fun getHeight(maxSizeXIn: Int, maxSizeYIn: Int): Int = 1

    override fun render(
        gui: ContainerScreen<DiaryContainer>,
        font: FontRenderer,
        xSize: Int,
        ySize: Int,
        x: Int,
        y: Int,
        depth: Int
    )
    {
        font.drawString(text.formattedText, (x + 15).toFloat(), (y + 15).toFloat(), color)
    }

    override fun toString(): String = text.formattedText
}
