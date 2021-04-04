package phoenix.client.render

import net.minecraft.client.renderer.entity.IEntityRenderer
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer
import net.minecraft.client.renderer.entity.model.EntityModel
import phoenix.enity.CaudaEntity
import phoenix.init.CaudaArmorItem

class CaudaArmorLayer<T : CaudaEntity, M : EntityModel<T>>(renderer: IEntityRenderer<T, M>, val t : T) : AbstractEyesLayer<T, M>(renderer)
{
    override fun getRenderType() = (t.getArmorStack().item as CaudaArmorItem).material.texture
}