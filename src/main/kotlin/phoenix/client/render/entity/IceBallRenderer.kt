package phoenix.client.render.entity

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.util.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import phoenix.MOD_ID
import phoenix.Phoenix
import phoenix.client.models.entity.IceBallModel
import phoenix.enity.projectile.IceBallEntity

@OnlyIn(Dist.CLIENT)
class IceBallRenderer(renderManagerIn: EntityRendererManager) : EntityRenderer<IceBallEntity>(renderManagerIn)
{
    val model = IceBallModel()

    override fun render(
        entityIn: IceBallEntity,
        entityYaw: Float,
        partialTicks: Float,
        matrixStackIn: MatrixStack,
        bufferIn: IRenderTypeBuffer,
        packedLightIn: Int
    )
    {
        matrixStackIn.push()
        RenderSystem.enableAlphaTest()
        RenderSystem.enableBlend()
        val buffer = bufferIn.getBuffer(TEXTURE_TYPE)
        model.render(matrixStackIn, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f)
        matrixStackIn.pop()
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn)
    }

    override fun getEntityTexture(entity: IceBallEntity): ResourceLocation = ICEEBALL_TEXTURE

    companion object
    {
        private val ICEEBALL_TEXTURE = ResourceLocation(MOD_ID, "textures/entity/iceball.png")
        private val TEXTURE_TYPE = RenderType.getEntityCutoutNoCull(ICEEBALL_TEXTURE)
    }
}
