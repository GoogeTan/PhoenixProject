package phoenix.client.render

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.Quaternion
import net.minecraft.client.renderer.tileentity.TileEntityRenderer
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import phoenix.client.models.block.OvenModel
import phoenix.init.PhoenixRenderTypes
import phoenix.tile.ash.OvenTile

class OvenRenderer(rendererDispatcherIn: TileEntityRendererDispatcher) : TileEntityRenderer<OvenTile>(rendererDispatcherIn)
{
    var model = OvenModel()
    override fun render(te: OvenTile, partialTicks: Float, matrixStackIn: MatrixStack, bufferIn: IRenderTypeBuffer, combinedLightIn: Int, combinedOverlayIn: Int)
    {
        val builder: IVertexBuilder = bufferIn.getBuffer(PhoenixRenderTypes.OVEN)
        matrixStackIn.rotate(Quaternion(180F, 0F, 0F, true))
        matrixStackIn.translate(0.5, -1.5, -0.5)
        model.render(matrixStackIn, builder, combinedLightIn, combinedOverlayIn, 0.5F, 0.5F, 0.5F, 0.5F)
    }
}