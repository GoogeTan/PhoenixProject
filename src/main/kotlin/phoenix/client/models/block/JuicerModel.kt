package phoenix.client.models.block

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.model.ItemCameraTransforms
import phoenix.other.itemRenderer
import phoenix.tile.redo.JuicerTile

class JuicerModel(tileTank: JuicerTile) : TankModel<JuicerTile>(tileTank)
{
    override fun render(matrixStackIn: MatrixStack, buffer: IRenderTypeBuffer, packedLightIn: Int, packedOverlayIn: Int)
    {
        super.render(matrixStackIn, buffer, packedLightIn, packedOverlayIn)
        itemRenderer?.renderItem(tileTank.stack, ItemCameraTransforms.TransformType.FIXED, packedLightIn, packedOverlayIn, matrixStackIn, buffer)
    }
}