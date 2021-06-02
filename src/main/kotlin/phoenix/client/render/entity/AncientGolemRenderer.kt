package phoenix.client.render.entity

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.entity.layers.HeldItemLayer
import net.minecraft.util.ResourceLocation
import phoenix.Phoenix
import phoenix.client.models.entity.AncientGolemModel
import phoenix.client.render.KMobRenderer
import phoenix.enity.AncientGolemEntity
import javax.annotation.Nonnull

class AncientGolemRenderer(renderManager: EntityRendererManager) : KMobRenderer<AncientGolemEntity, AncientGolemModel>(renderManager, AncientGolemModel(), 0.5f)
{
    @Nonnull
    override fun getEntityTexture(entity: AncientGolemEntity) = ResourceLocation(Phoenix.MOD_ID, "textures/entity/ancient_golem.png")

    init
    {
        addLayer(GolemHeldItemLayer())
    }

    inner class GolemHeldItemLayer: HeldItemLayer<AncientGolemEntity, AncientGolemModel>(this@AncientGolemRenderer)
    {
        override fun render(
            matrixStackIn: MatrixStack,
            bufferIn: IRenderTypeBuffer,
            packedLightIn: Int,
            entity: AncientGolemEntity,
            limbSwing: Float,
            limbSwingAmount: Float,
            partialTicks: Float,
            ageInTicks: Float,
            netHeadYaw: Float,
            headPitch: Float
        )
        {
            if (!entity.closed)
            {
                super.render(matrixStackIn, bufferIn, packedLightIn, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch)
            }
        }
    }
}

