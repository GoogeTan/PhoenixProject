package phoenix.client.render.entity

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.Vector3f
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.model.ItemCameraTransforms
import net.minecraft.client.renderer.texture.AtlasTexture.LOCATION_BLOCKS_TEXTURE
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import phoenix.enity.projectile.KnifeEntity
import phoenix.init.PhxItems.ZIRCONIUM_KNIFE
import phoenix.other.itemRenderer

class KnifeRenderer<T : KnifeEntity>(renderManagerIn: EntityRendererManager) : EntityRenderer<T>(renderManagerIn)
{
    private val scale = 1f

    override fun render(entityIn: T, entityYaw: Float, partialTicks: Float, matrixStackIn: MatrixStack, bufferIn: IRenderTypeBuffer, packedLightIn: Int)
    {
        matrixStackIn.push()
        matrixStackIn.scale(scale, scale, scale)
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90.0f))
        matrixStackIn.rotate(Vector3f.XN.rotationDegrees(90.0f))
        itemRenderer?.renderItem(ItemStack(ZIRCONIUM_KNIFE), ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn)
        matrixStackIn.pop()
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn)
    }

    override fun getEntityTexture(entity: T): ResourceLocation = LOCATION_BLOCKS_TEXTURE
}
