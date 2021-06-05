package phoenix.client.render

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.tileentity.TileEntityRenderer
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import phoenix.client.models.block.TankModel
import phoenix.tile.redo.TankTile

class TankRenderer<T : TankTile>(rendererDispatcherIn: TileEntityRendererDispatcher) : TileEntityRenderer<T>(rendererDispatcherIn)
{
    override fun render(te: T, partialTicks: Float, matrixStackIn: MatrixStack, bufferIn: IRenderTypeBuffer, combinedLightIn: Int, combinedOverlayIn: Int) = TankModel(te).render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn)
}