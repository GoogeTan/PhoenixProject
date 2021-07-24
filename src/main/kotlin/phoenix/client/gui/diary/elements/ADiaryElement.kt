package phoenix.client.gui.diary.elements

import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import phoenix.containers.DiaryContainer

abstract class ADiaryElement
{
    abstract fun getHeight(maxSizeXIn: Int, maxSizeYIn: Int): Int
    abstract fun render(
        gui: ContainerScreen<DiaryContainer>,
        font: FontRenderer,
        xSize: Int,
        ySize: Int,
        x: Int,
        y: Int,
        depth: Int
    )

    abstract override fun toString(): String
}