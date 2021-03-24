package phoenix.client.models.entity

import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.IEntityRenderer
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer
import net.minecraft.entity.LivingEntity
import net.minecraft.util.ResourceLocation

class CaudaEyesLayer<T : LivingEntity>(rendererIn: IEntityRenderer<T, Cauda2Model<T>>) : AbstractEyesLayer<T, Cauda2Model<T>>(rendererIn)
{
    override fun getRenderType(): RenderType
    {
        return RENDER_TYPE
    }

    companion object
    {
        private val RENDER_TYPE = RenderType.getEyes(ResourceLocation("phoenix", "textures/entity/cauda/cauda_eyes.png"))
    }
}