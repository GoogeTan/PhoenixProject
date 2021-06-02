package phoenix.client.render.entity.layer

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.entity.IEntityRenderer
import net.minecraft.client.renderer.entity.layers.LayerRenderer
import net.minecraft.client.renderer.entity.model.EntityModel
import net.minecraft.client.renderer.texture.OverlayTexture
import phoenix.client.models.entity.CaudaModel
import phoenix.enity.CaudaEntity
import phoenix.init.CaudaArmorItem

class CaudaArmorLayer<T : CaudaEntity, M : EntityModel<T>>(renderer: IEntityRenderer<T, M>) : LayerRenderer<T, M>(renderer)
{
    override fun render(
        matrixStackIn: MatrixStack,
        bufferIn: IRenderTypeBuffer,
        packedLightIn: Int,
        entitylivingbaseIn: T,
        limbSwing: Float,
        limbSwingAmount: Float,
        partialTicks: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    )
    {
        val armor = entitylivingbaseIn.getArmorStack().item
        if(armor is CaudaArmorItem)
        {
            val renderType = armor.material.texture.getRenderType()
            val model = entityModel
            model.render(matrixStackIn, bufferIn.getBuffer(renderType), 15728640, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f)
            if (model is CaudaModel)
            {
                matrixStackIn.scale(1.05f, 1.05f, 1.05f)
                model.head.render(matrixStackIn, bufferIn.getBuffer(renderType), 15728640, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f)
                matrixStackIn.scale(1.0f / 1.05f, 1.0f / 1.05f, 1.0f / 1.05f)
            }
        }
    }
}