package phoenix.client.render.entity

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.Vector3f
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.util.ResourceLocation
import phoenix.MOD_ID
import phoenix.Phoenix
import phoenix.client.models.entity.CaudaModel
import phoenix.client.render.entity.layer.CaudaArmorLayer
import phoenix.client.render.entity.layer.SimpleEyesLayer
import phoenix.enity.CaudaEntity
import phoenix.init.PhxRenderTypes
import javax.annotation.Nonnull

class CaudaRenderer(renderManager: EntityRendererManager) : MobRenderer<CaudaEntity, CaudaModel>(renderManager, CaudaModel(), 1f)
{
    @Nonnull
    override fun getEntityTexture(entity: CaudaEntity) = ResourceLocation(MOD_ID, "textures/entity/cauda/texture_.png")

    override fun applyRotations(
        entity: CaudaEntity,
        matrixStackIn: MatrixStack,
        ageInTicks: Float,
        rotationYaw: Float,
        partialTicks: Float
    )
    {
        super.applyRotations(entity, matrixStackIn, ageInTicks, rotationYaw, partialTicks)
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(entity.rotationPitch))
    }

    override fun preRenderCallback(entity: CaudaEntity, matrixStackIn: MatrixStack, partialTickTime: Float)
    {
        val scale = if (entity.isChild) 0.75f else 3.0f
        matrixStackIn.scale(scale, scale, scale)
    }

    init
    {
        addLayer(SimpleEyesLayer(this, PhxRenderTypes.eyesTexture))
        addLayer(CaudaArmorLayer(this))
    }
}

