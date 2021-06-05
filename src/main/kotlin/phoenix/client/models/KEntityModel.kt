package phoenix.client.models

import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.model.EntityModel
import net.minecraft.entity.LivingEntity
import net.minecraft.util.ResourceLocation
import java.util.function.Function

abstract class KEntityModel<T : LivingEntity> : EntityModel<T>
{
    constructor() : super()
    constructor(func: Function<ResourceLocation, RenderType>) : super(func)

    abstract fun setRotationAngles(entity: T, limbSwing: Float, limbSwingAmount: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, partialTick : Float)

    final override fun setRotationAngles(entityIn: T, limbSwing: Float, limbSwingAmount: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float) = throw UnsupportedOperationException()
}