package phoenix.client.gui.diary.elements

import net.minecraft.client.gui.FontRenderer
import phoenix.client.gui.DiaryGui

class RightAlignedTextElement : TextElement
{
    constructor(text: String) : super(text)
    {
    }

    constructor(text: String, color: Int) : super(text, color)
    {
    }

    override fun render(
        gui: DiaryGui,
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