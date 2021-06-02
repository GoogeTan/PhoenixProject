package phoenix.client.render

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.MobEntity
import net.minecraft.entity.Pose
import net.minecraft.util.math.MathHelper
import net.minecraftforge.client.event.RenderLivingEvent
import net.minecraftforge.client.event.RenderNameplateEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.Event
import phoenix.client.models.KEntityModel

abstract class KMobRenderer<T : MobEntity, M : KEntityModel<T>>(renderManagerIn: EntityRendererManager, entityModelIn: M, shadowSizeIn: Float) : MobRenderer<T, M>(renderManagerIn, entityModelIn, shadowSizeIn)
{
    override fun render
        (
            entityIn: T,
            entityYaw: Float,
            partialTicks: Float,
            matrixStackIn: MatrixStack,
            bufferIn: IRenderTypeBuffer,
            packedLightIn: Int
        )
    {
        if (MinecraftForge.EVENT_BUS.post(RenderLivingEvent.Pre(entityIn, this, partialTicks, matrixStackIn, bufferIn, packedLightIn))) return
        matrixStackIn.push()
        entityModel.swingProgress = getSwingProgress(entityIn, partialTicks)

        val shouldSit = entityIn.isPassenger && entityIn.ridingEntity != null && entityIn.ridingEntity!!.shouldRiderSit()
        entityModel.isSitting = shouldSit
        entityModel.isChild = entityIn.isChild
        var f = MathHelper.interpolateAngle(partialTicks, entityIn.prevRenderYawOffset, entityIn.renderYawOffset)
        val f1 = MathHelper.interpolateAngle(partialTicks, entityIn.prevRotationYawHead, entityIn.rotationYawHead)
        var netHeadYaw = f1 - f
        if (shouldSit && entityIn.ridingEntity is LivingEntity)
        {
            val sitting = entityIn.ridingEntity as LivingEntity
            f = MathHelper.interpolateAngle(partialTicks, sitting.prevRenderYawOffset, sitting.renderYawOffset)
            netHeadYaw = f1 - f
            var netHeadYawWarped = MathHelper.wrapDegrees(netHeadYaw)
            if (netHeadYawWarped < -85.0f)
            {
                netHeadYawWarped = -85.0f
            }
            if (netHeadYawWarped >= 85.0f)
            {
                netHeadYawWarped = 85.0f
            }
            f = f1 - netHeadYawWarped
            if (netHeadYawWarped * netHeadYawWarped > 2500.0f)
            {
                f += netHeadYawWarped * 0.2f
            }
            netHeadYaw = f1 - f
        }

        val headPitch = MathHelper.lerp(partialTicks, entityIn.prevRotationPitch, entityIn.rotationPitch)
        if (entityIn.pose == Pose.SLEEPING)
        {
            val direction = entityIn.bedDirection
            if (direction != null)
            {
                val f4 = entityIn.getEyeHeight(Pose.STANDING) - 0.1f
                matrixStackIn.translate(
                    ((-direction.xOffset).toFloat() * f4).toDouble(), 0.0,
                    ((-direction.zOffset).toFloat() * f4).toDouble()
                )
            }
        }

        val ageInTicks = handleRotationFloat(entityIn, partialTicks)
        applyRotations(entityIn, matrixStackIn, ageInTicks, f, partialTicks)
        matrixStackIn.scale(-1.0f, -1.0f, 1.0f)
        preRenderCallback(entityIn, matrixStackIn, partialTicks)
        matrixStackIn.translate(0.0, (-1.501f).toDouble(), 0.0)
        var limbSwingAmount = 0.0f
        var limbSwing = 0.0f
        if (!shouldSit && entityIn.isAlive)
        {
            limbSwingAmount = MathHelper.lerp(partialTicks, entityIn.prevLimbSwingAmount, entityIn.limbSwingAmount)
            limbSwing = entityIn.limbSwing - entityIn.limbSwingAmount * (1.0f - partialTicks)
            if (entityIn.isChild)
            {
                limbSwing *= 3.0f
            }
            if (limbSwingAmount > 1.0f)
            {
                limbSwingAmount = 1.0f
            }
        }

        entityModel.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTicks)
        entityModel.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks)
        val isVisible = isVisible(entityIn)
        val flag1 = !isVisible && !entityIn.isInvisibleToPlayer(Minecraft.getInstance().player!!)
        val renderType = func_230042_a_(entityIn, isVisible, flag1)
        if (renderType != null)
        {
            val buffer = bufferIn.getBuffer(renderType)
            val packedOverlayIn = getPackedOverlay(entityIn, getOverlayProgress(entityIn, partialTicks))
            entityModel.render(matrixStackIn, buffer, packedLightIn, packedOverlayIn, 1.0f, 1.0f, 1.0f, if (flag1) 0.15f else 1.0f)
        }

        if (!entityIn.isSpectator)
        {
            for (layer in layerRenderers)
            {
                layer.render(matrixStackIn, bufferIn, packedLightIn, entityIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch)
            }
        }

        matrixStackIn.pop()
        val renderNameplateEvent = RenderNameplateEvent(entityIn, entityIn.displayName.formattedText, this, matrixStackIn, bufferIn, packedLightIn)
        MinecraftForge.EVENT_BUS.post(renderNameplateEvent)
        if (renderNameplateEvent.result != Event.Result.DENY && (renderNameplateEvent.result == Event.Result.ALLOW || canRenderName(entityIn)))
        {
            renderName(entityIn, renderNameplateEvent.content, matrixStackIn, bufferIn, packedLightIn)
        }
        MinecraftForge.EVENT_BUS.post(RenderLivingEvent.Post(entityIn, this, partialTicks, matrixStackIn, bufferIn, packedLightIn))
    }
}