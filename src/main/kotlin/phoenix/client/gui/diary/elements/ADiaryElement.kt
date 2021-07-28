package phoenix.client.gui.diary.elements

import net.minecraft.client.gui.FontRenderer
import phoenix.client.gui.DiaryGui

abstract class ADiaryElement
{
    abstract fun getHeight(maxSizeXIn: Int, maxSizeYIn: Int): Int
    abstract fun render(
        gui: DiaryGui,
        font: FontRenderer,
        xSize: Int,
        ySize: Int,
        x: Int,
        y: Int,
        depth: Int
    )

    abstract override fun toString(): String
}