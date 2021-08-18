package phoenix.client.gui.diary.elements

import net.minecraft.client.gui.FontRenderer
import net.minecraft.util.text.ITextComponent
import phoenix.client.gui.DiaryGui

class CenterAlignedTextElement : TextElement
{
    constructor(string : ITextComponent) : super(string)
    constructor(string : String) : super(string)
    constructor(string : String, color : Int) : super(string, color)

    override fun render(gui: DiaryGui, font: FontRenderer, xSize: Int, ySize: Int, x: Int, y: Int, depth: Int)
    {
        super.render(gui, font, xSize, ySize, x + xSize / 2 / 2 - font.getStringWidth(text.formattedText) / 2, y, depth)
    }

    override fun toString(): String
    {
        return "[c]" + super.toString() + "[C]"
    }
}