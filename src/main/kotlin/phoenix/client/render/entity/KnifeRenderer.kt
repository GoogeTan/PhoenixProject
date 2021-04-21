package phoenix.client.render.entity

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.ItemRenderer
import net.minecraft.client.renderer.Vector3f
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.model.ItemCameraTransforms
import net.minecraft.client.renderer.texture.AtlasTexture
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import phoenix.enity.KnifeEntity
import phoenix.init.PhxItems.ZIRCONIUM_KNIFE

class KnifeRenderer<T : KnifeEntity>(renderManagerIn: EntityRendererManager) : EntityRenderer<T>(renderManagerIn)
{
    private val itemRenderer: ItemRenderer = Minecraft.getInstance().getItemRenderer()

    private val scale = 1f
    override fun getBlockLight(entityIn: T, partialTicks: Float): Int
    {
        return super.getBlockLight(entityIn, partialTicks)
    }

    override fun render(entityIn: T, entityYaw: Float, partialTicks: Float, matrixStackIn: MatrixStack, bufferIn: IRenderTypeBuffer, packedLightIn: Int)
    {
        matrixStackIn.push()
        matrixStackIn.scale(scale, scale, scale)
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90.0f))
        matrixStackIn.rotate(Vector3f.XN.rotationDegrees(90.0f))
        itemRenderer.renderItem(ItemStack(ZIRCONIUM_KNIFE), ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn)
        matrixStackIn.pop()
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn)
    }

    override fun getEntityTexture(entity: T): ResourceLocation
    {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE
    }

}
