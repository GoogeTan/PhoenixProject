package phoenix.client.render

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.tileentity.TileEntityRenderer
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import phoenix.client.models.block.TankModel
import phoenix.tile.redo.TankTile

class TankRenderer(rendererDispatcherIn: TileEntityRendererDispatcher) : TileEntityRenderer<TankTile>(rendererDispatcherIn)
{
    override fun render(
        te: TankTile,
        partialTicks: Float,
        matrixStackIn: MatrixStack,
        bufferIn: IRenderTypeBuffer,
        combinedLightIn: Int,
        combinedOverlayIn: Int
    )
    {
        TankModel(te).render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn)
    }
}