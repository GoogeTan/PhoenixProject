package phoenix.client.gui.diary.elements

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.FontRenderer
import phoenix.client.gui.DiaryGui
import phoenix.other.TextureLocation
import phoenix.other.drawRectScalable
import phoenix.other.textureManager
import kotlin.math.ceil

class ImageElement(private val img: TextureLocation, var w: Int, var h: Int) : ADiaryElement()
{
    override fun getHeight(maxSizeXIn: Int, maxSizeYIn: Int): Int
    {
        val scale = scale(maxSizeXIn / 2, maxSizeYIn / 2)
        return ceil(h * scale).toInt()
    }

    fun scale(maxSizeX: Int, maxSizeY: Int): Double
    {
        return if (w != 0 && h != 0)
        {
            var scale = maxSizeX / w.toDouble()
            val sizeX: Double = (w * scale).toInt().toDouble()
            val sizeY: Double = (h * scale).toInt().toDouble()
            if (maxSizeY < sizeY)
            {
                scale *= maxSizeY.toDouble() / sizeY
            }
            scale
        } else
        {
            1.0
        }
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
        val scale = scale(xSize, ySize)
        RenderSystem.pushMatrix()
        RenderSystem.scaled(scale, scale, scale)
        textureManager?.bindTexture(img)
        drawRectScalable(img, x + 15, y + 15, xSize.toDouble(), ySize.toDouble(), depth)
        RenderSystem.scaled(1 / scale, 1 / scale, 1 / scale)
        RenderSystem.popMatrix()
    }

    override fun toString(): String = img.toString()
}
