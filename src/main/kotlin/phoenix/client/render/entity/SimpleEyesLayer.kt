package phoenix.client.render.entity

import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.IEntityRenderer
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer
import net.minecraft.client.renderer.entity.model.EntityModel
import net.minecraft.entity.Entity

class SimpleEyesLayer<T : Entity, M : EntityModel<T>>(renderer: IEntityRenderer<T, M>, private val texture : RenderType) : AbstractEyesLayer<T, M>(renderer)
{
    override fun getRenderType() = texture
}