package phoenix.client.render

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.model.Material
import net.minecraft.client.renderer.texture.AtlasTexture
import net.minecraft.client.renderer.tileentity.TileEntityRenderer
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import net.minecraft.util.ResourceLocation
import phoenix.client.models.block.PipeModel
import phoenix.init.PhoenixRenderTypes
import phoenix.tile.redo.PipeTile
import phoenix.utils.RenderUtils.refreshDrawing

class PipeRender(rendererDispatcherIn: TileEntityRendererDispatcher) : TileEntityRenderer<PipeTile>(rendererDispatcherIn)
{
    override fun render(
        tile: PipeTile,
        partialTicks: Float,
        matrix: MatrixStack,
        buffer: IRenderTypeBuffer,
        light: Int,
        overlay: Int
    )
    {
        val model = PipeModel(tile.blockState)
        matrix.push()
        val builder = buffer.getBuffer(PhoenixRenderTypes.PIPE)
        model.render(matrix, builder, light, overlay, 1.0f, 1.0f, 1.0f, 1.0f)
        refreshDrawing(builder, PhoenixRenderTypes.PIPE)
        matrix.pop()
    }

    companion object
    {
        val TEXTURE = Material(
            AtlasTexture.LOCATION_BLOCKS_TEXTURE,
            ResourceLocation("block/chorus_plant")
        )
    }
}