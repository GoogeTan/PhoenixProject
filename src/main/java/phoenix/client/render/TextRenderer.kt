package phoenix.client.render

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.model.Material
import net.minecraft.client.renderer.texture.AtlasTexture
import net.minecraft.client.renderer.texture.NativeImage
import net.minecraft.client.renderer.tileentity.TileEntityRenderer
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import net.minecraft.util.ResourceLocation
import phoenix.client.models.block.TextModel
import phoenix.tile.TextTile

class TextRenderer(dis: TileEntityRendererDispatcher?) : TileEntityRenderer<TextTile>(dis)
{
    var model = TextModel()
    override fun render(p0: TextTile, p1: Float, matrics: MatrixStack, p3: IRenderTypeBuffer, p4: Int, p5: Int)
    {
        val fontrenderer = Minecraft.getInstance().fontRenderer
        val vertex = Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE, ResourceLocation("block/clay")).getBuffer(p3, RenderType::getEntitySolid)
        //model.render(matrics, vertex, p4, p5)
        val i: Int? = p0.text.style.color?.color
        if(i != null)
        {
            val j = (NativeImage.getRed(i).toDouble() * 0.4).toInt()
            val k = (NativeImage.getGreen(i).toDouble() * 0.4).toInt()
            val l = (NativeImage.getBlue(i).toDouble() * 0.4).toInt()
            val i1 = NativeImage.getCombined(0, l, k, j)
            for (j1 in 0..3)
            {
                val s: String = p0.text.toString()
                fontrenderer.renderString(s, ((-fontrenderer.getStringWidth(s) / 2).toFloat()), (j1 * 10 - p0.text.toString().length * 5).toFloat(), i1, false, matrics.getLast().getMatrix(), p3, false, 0, p4)
            }
        }
    }
}