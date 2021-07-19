package phoenix.client.gui.diary.elements

import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import phoenix.containers.DiaryContainer

class RightAlignedTextElement : TextElement
{
    constructor(text: String) : super(text)
    {
    }

    constructor(text: String, color: Int) : super(text, color)
    {
    }

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
        super.render(gui, font, xSize, ySize, x + xSize - 25 - font.getStringWidth(text.formattedText), y, depth)
    }

    override fun toString(): String
    {
        return "[r]$text[R]"
    }
}