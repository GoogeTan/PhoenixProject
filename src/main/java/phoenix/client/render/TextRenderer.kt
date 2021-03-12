package phoenix.client.render

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.Quaternion
import net.minecraft.client.renderer.tileentity.TileEntityRenderer
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import phoenix.client.models.block.TextModel
import phoenix.tile.TextTile

class TextRenderer(dis: TileEntityRendererDispatcher) : TileEntityRenderer<TextTile>(dis)
{
    var model = TextModel()

    override fun render(
        tile: TextTile,
        p1: Float,
        matrics: MatrixStack,
        bufferIn: IRenderTypeBuffer,
        combinedLightIn : Int,
        combinedOverlayIn : Int
    )
    {

        val font = renderDispatcher.getFontRenderer()

        matrics.push()

        matrics.translate(0.5, -0.135, 1.0)
        val scale = 0.010416667f
        matrics.scale(scale, -scale, scale)
        for (y in 0..5)
        {
            val s = "I love minecraft 0"
            val f3 = (-font.getStringWidth(s) / 2).toFloat()
            font.renderString(s, f3, (y * 10 - s.length * 5).toFloat(), 0, false, matrics.last.matrix, bufferIn, false, 0, combinedLightIn)
        }


        matrics.pop()

        matrics.push()
        matrics.translate(-0.5, -0.0, 0.0)
        matrics.rotate(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))

        matrics.scale(scale, -scale, scale)
        for (y in 0..5)
        {
            val s = "I love minecraft 2"
            val f3 = (-font.getStringWidth(s) / 2).toFloat()
            font.renderString(s, f3, (y * 10 - s.length * 5).toFloat(), 0, false, matrics.last.matrix, bufferIn, false, 0, combinedLightIn)
        }
        matrics.pop()

        matrics.push()
        matrics.rotate(Quaternion(0.0f, 1.0f, 0.0f, 0.0f))
        matrics.translate(-0.5, -0.0, 0.0)
        matrics.scale(scale, -scale, scale)
        for (y in 0..5)
        {
            val s = "I love minecraft 2"
            val f3 = (-font.getStringWidth(s) / 2).toFloat()
            font.renderString(s, f3, (y * 10 - s.length * 5).toFloat(), 0, false, matrics.last.matrix, bufferIn, false, 0, combinedLightIn)
        }
        matrics.pop()
    }
}