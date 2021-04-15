package phoenix.client.render

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.Matrix4f
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.Vector3f
import net.minecraft.client.renderer.entity.EnderCrystalRenderer
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.entity.model.EntityModel
import net.minecraft.client.renderer.model.ModelRenderer
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.entity.boss.dragon.EnderDragonEntity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.MathHelper
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import phoenix.enity.boss.DragonAshStageEntity
import java.util.*
import kotlin.math.sin

@OnlyIn(Dist.CLIENT)
class AshDragonRenderer(renderManagerIn: EntityRendererManager) :
    EntityRenderer<DragonAshStageEntity>(renderManagerIn)
{
    private val model = EnderDragonModel()
    override fun render(
        entityIn: DragonAshStageEntity,
        entityYaw: Float,
        partialTicks: Float,
        matrixStackIn: MatrixStack,
        bufferIn: IRenderTypeBuffer,
        packedLightIn: Int
    )
    {
        matrixStackIn.push()
        val f = entityIn.getMovementOffsets(7, partialTicks)[0].toFloat()
        val f1 =
            (entityIn.getMovementOffsets(5, partialTicks)[1] - entityIn.getMovementOffsets(
                10,
                partialTicks
            )[1]).toFloat()
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-f))
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(f1 * 10.0f))
        matrixStackIn.translate(0.0, 0.0, 1.0)
        matrixStackIn.scale(-1.0f, -1.0f, 1.0f)
        matrixStackIn.translate(0.0, (-1.501f).toDouble(), 0.0)
        val flag = entityIn.hurtTime > 0
        model.setLivingAnimations(entityIn, 0.0f, 0.0f, partialTicks)
        if (entityIn.deathTicks > 0)
        {
            val f2 = entityIn.deathTicks.toFloat() / 200.0f
            val ivertexbuilder = bufferIn.getBuffer(
                RenderType.getEntityAlpha(
                    DRAGON_EXPLODING_TEXTURES, f2
                )
            )
            model.render(
                matrixStackIn,
                ivertexbuilder,
                packedLightIn,
                OverlayTexture.NO_OVERLAY,
                1.0f,
                1.0f,
                1.0f,
                1.0f
            )
            val ivertexbuilder1 = bufferIn.getBuffer(field_229054_i_)
            model.render(
                matrixStackIn,
                ivertexbuilder1,
                packedLightIn,
                OverlayTexture.getPackedUV(0.0f, flag),
                1.0f,
                1.0f,
                1.0f,
                1.0f
            )
        } else
        {
            val ivertexbuilder3 = bufferIn.getBuffer(field_229053_h_)
            model.render(
                matrixStackIn,
                ivertexbuilder3,
                packedLightIn,
                OverlayTexture.getPackedUV(0.0f, flag),
                1.0f,
                1.0f,
                1.0f,
                1.0f
            )
        }
        val ivertexbuilder4 = bufferIn.getBuffer(field_229055_j_)
        model.render(matrixStackIn, ivertexbuilder4, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f)
        if (entityIn.deathTicks > 0)
        {
            val f5 = (entityIn.deathTicks.toFloat() + partialTicks) / 200.0f
            var f7 = 0.0f
            if (f5 > 0.8f)
            {
                f7 = (f5 - 0.8f) / 0.2f
            }
            val random = Random(432L)
            val ivertexbuilder2 = bufferIn.getBuffer(RenderType.getLightning())
            matrixStackIn.push()
            matrixStackIn.translate(0.0, -1.0, -2.0)
            var i = 0
            while (i.toFloat() <(f5 + f5 * f5) / 2.0f * 60.0f)
            {
                matrixStackIn.rotate(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0f))
                matrixStackIn.rotate(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0f))
                matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0f))
                matrixStackIn.rotate(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0f))
                matrixStackIn.rotate(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0f))
                matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0f + f5 * 90.0f))
                val f3 = random.nextFloat() * 20.0f + 5.0f + f7 * 10.0f
                val f4 = random.nextFloat() * 2.0f + 1.0f + f7 * 2.0f
                val matrix4f = matrixStackIn.last.matrix
                val j = (255.0f * (1.0f - f7)).toInt()
                func_229061_a_(ivertexbuilder2, matrix4f, j)
                func_229060_a_(ivertexbuilder2, matrix4f, f3, f4)
                func_229062_b_(ivertexbuilder2, matrix4f, f3, f4)
                func_229061_a_(ivertexbuilder2, matrix4f, j)
                func_229062_b_(ivertexbuilder2, matrix4f, f3, f4)
                func_229063_c_(ivertexbuilder2, matrix4f, f3, f4)
                func_229061_a_(ivertexbuilder2, matrix4f, j)
                func_229063_c_(ivertexbuilder2, matrix4f, f3, f4)
                func_229060_a_(ivertexbuilder2, matrix4f, f3, f4)
                ++i
            }
            matrixStackIn.pop()
        }
        matrixStackIn.pop()
        if (entityIn.closestEnderCrystal != null)
        {
            matrixStackIn.push()
            val f6 = (entityIn.closestEnderCrystal!!.posX - MathHelper.lerp(
                partialTicks.toDouble(),
                entityIn.prevPosX,
                entityIn.posX
            )).toFloat()
            val f8 = (entityIn.closestEnderCrystal!!.posY - MathHelper.lerp(
                partialTicks.toDouble(),
                entityIn.prevPosY,
                entityIn.posY
            )).toFloat()
            val f9 = (entityIn.closestEnderCrystal!!.posZ - MathHelper.lerp(
                partialTicks.toDouble(),
                entityIn.prevPosZ,
                entityIn.posZ
            )).toFloat()
            func_229059_a_(
                f6,
                f8 + EnderCrystalRenderer.func_229051_a_(entityIn.closestEnderCrystal, partialTicks),
                f9,
                partialTicks,
                entityIn.ticksExisted,
                matrixStackIn,
                bufferIn,
                packedLightIn
            )
            matrixStackIn.pop()
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn)
    }

    /**
     * Returns the location of an entity's texture.
     */
    override fun getEntityTexture(entity: DragonAshStageEntity): ResourceLocation
    {
        return DRAGON_TEXTURES
    }

    @OnlyIn(Dist.CLIENT)
    class EnderDragonModel : EntityModel<DragonAshStageEntity>()
    {
        private val head: ModelRenderer
        private val spine: ModelRenderer
        private val jaw: ModelRenderer
        private val body: ModelRenderer
        private val leftProximalWing: ModelRenderer
        private val leftDistalWing: ModelRenderer
        private val leftForeThigh: ModelRenderer
        private val leftForeLeg: ModelRenderer
        private val leftForeFoot: ModelRenderer
        private val leftHindThigh: ModelRenderer
        private val leftHindLeg: ModelRenderer
        private val leftHindFoot: ModelRenderer
        private val rightProximalWing: ModelRenderer
        private val rightDistalWing: ModelRenderer
        private val rightForeThigh: ModelRenderer
        private val rightForeLeg: ModelRenderer
        private val rightForeFoot: ModelRenderer
        private val rightHindThigh: ModelRenderer
        private val rightHindLeg: ModelRenderer
        private val rightHindFoot: ModelRenderer
        private var dragonInstance: DragonAshStageEntity? = null
        private var partialTicks = 0f
        override fun setLivingAnimations(
            entityIn: DragonAshStageEntity,
            limbSwing: Float,
            limbSwingAmount: Float,
            partialTick: Float
        )
        {
            dragonInstance = entityIn
            partialTicks = partialTick
        }

        /**
         * Sets this entity's model rotation angles
         */
        override fun setRotationAngles(
            entityIn: DragonAshStageEntity,
            limbSwing: Float,
            limbSwingAmount: Float,
            ageInTicks: Float,
            netHeadYaw: Float,
            headPitch: Float
        )
        {
        }

        override fun render(
            matrixStackIn: MatrixStack,
            bufferIn: IVertexBuilder,
            packedLightIn: Int,
            packedOverlayIn: Int,
            red: Float,
            green: Float,
            blue: Float,
            alpha: Float
        )
        {
            matrixStackIn.push()
            val f = MathHelper.lerp(partialTicks, dragonInstance!!.prevAnimTime, dragonInstance!!.animTime)
            jaw.rotateAngleX = (sin((f * (Math.PI.toFloat() * 2f)).toDouble()) + 1.0).toFloat() * 0.2f
            var f1 = (sin((f * (Math.PI.toFloat() * 2f) - 1.0f).toDouble()) + 1.0).toFloat()
            f1 = (f1 * f1 + f1 * 2.0f) * 0.05f
            matrixStackIn.translate(0.0, (f1 - 2.0f).toDouble(), -3.0)
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(f1 * 2.0f))
            var f2 = 0.0f
            var f3 = 20.0f
            var f4 = -12.0f
            val f5 = 1.5f
            var adouble = dragonInstance!!.getMovementOffsets(6, partialTicks)
            val f6 = MathHelper.rotWrap(
                dragonInstance!!.getMovementOffsets(5, partialTicks)[0] - dragonInstance!!.getMovementOffsets(
                    10,
                    partialTicks
                )[0]
            )
            val f7 =
                MathHelper.rotWrap(dragonInstance!!.getMovementOffsets(5, partialTicks)[0] + (f6 / 2.0f).toDouble())
            var f8 = f * (Math.PI.toFloat() * 2f)
            for (i in 0..4)
            {
                val adouble1 = dragonInstance!!.getMovementOffsets(5 - i, partialTicks)
                val f9 = Math.cos((i.toFloat() * 0.45f + f8).toDouble()).toFloat() * 0.15f
                spine.rotateAngleY = MathHelper.rotWrap(adouble1[0] - adouble[0]) * (Math.PI.toFloat() / 180f) * 1.5f
                spine.rotateAngleX = f9 + dragonInstance!!.getHeadPartYOffset(
                    i,
                    adouble,
                    adouble1
                ) * (Math.PI.toFloat() / 180f) * 1.5f * 5.0f
                spine.rotateAngleZ =
                    -MathHelper.rotWrap(adouble1[0] - f7.toDouble()) * (Math.PI.toFloat() / 180f) * 1.5f
                spine.rotationPointY = f3
                spine.rotationPointZ = f4
                spine.rotationPointX = f2
                f3 = (f3.toDouble() + Math.sin(spine.rotateAngleX.toDouble()) * 10.0).toFloat()
                f4 =
                    (f4.toDouble() - Math.cos(spine.rotateAngleY.toDouble()) * Math.cos(spine.rotateAngleX.toDouble()) * 10.0).toFloat()
                f2 =
                    (f2.toDouble() - Math.sin(spine.rotateAngleY.toDouble()) * Math.cos(spine.rotateAngleX.toDouble()) * 10.0).toFloat()
                spine.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn)
            }
            head.rotationPointY = f3
            head.rotationPointZ = f4
            head.rotationPointX = f2
            var adouble2 = dragonInstance!!.getMovementOffsets(0, partialTicks)
            head.rotateAngleY = MathHelper.rotWrap(adouble2[0] - adouble[0]) * (Math.PI.toFloat() / 180f)
            head.rotateAngleX = MathHelper.rotWrap(
                dragonInstance!!.getHeadPartYOffset(6, adouble, adouble2).toDouble()
            ) * (Math.PI.toFloat() / 180f) * 1.5f * 5.0f
            head.rotateAngleZ = -MathHelper.rotWrap(adouble2[0] - f7.toDouble()) * (Math.PI.toFloat() / 180f)
            head.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn)
            matrixStackIn.push()
            matrixStackIn.translate(0.0, 1.0, 0.0)
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-f6 * 1.5f))
            matrixStackIn.translate(0.0, -1.0, 0.0)
            body.rotateAngleZ = 0.0f
            body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn)
            val f10 = f * (Math.PI.toFloat() * 2f)
            leftProximalWing.rotateAngleX = 0.125f - Math.cos(f10.toDouble()).toFloat() * 0.2f
            leftProximalWing.rotateAngleY = -0.25f
            leftProximalWing.rotateAngleZ = -(Math.sin(f10.toDouble()) + 0.125).toFloat() * 0.8f
            leftDistalWing.rotateAngleZ = (Math.sin((f10 + 2.0f) as Double) + 0.5).toFloat() * 0.75f
            rightProximalWing.rotateAngleX = leftProximalWing.rotateAngleX
            rightProximalWing.rotateAngleY = -leftProximalWing.rotateAngleY
            rightProximalWing.rotateAngleZ = -leftProximalWing.rotateAngleZ
            rightDistalWing.rotateAngleZ = -leftDistalWing.rotateAngleZ
            func_229081_a_(
                matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, f1,
                leftProximalWing,
                leftForeThigh, leftForeLeg, leftForeFoot, leftHindThigh, leftHindLeg, leftHindFoot
            )
            func_229081_a_(
                matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, f1,
                rightProximalWing,
                rightForeThigh, rightForeLeg, rightForeFoot, rightHindThigh, rightHindLeg, rightHindFoot
            )
            matrixStackIn.pop()
            var f11 = -Math.sin((f * (Math.PI.toFloat() * 2f)).toDouble()).toFloat() * 0.0f
            f8 = f * (Math.PI.toFloat() * 2f)
            f3 = 10.0f
            f4 = 60.0f
            f2 = 0.0f
            adouble = dragonInstance!!.getMovementOffsets(11, partialTicks)
            for (j in 0..11)
            {
                adouble2 = dragonInstance!!.getMovementOffsets(12 + j, partialTicks)
                f11 = (f11.toDouble() + Math.sin((j.toFloat() * 0.45f + f8).toDouble()) * 0.05f.toDouble()).toFloat()
                spine.rotateAngleY =
                    (MathHelper.rotWrap(adouble2[0] - adouble[0]) * 1.5f + 180.0f) * (Math.PI.toFloat() / 180f)
                spine.rotateAngleX =
                    f11 + (adouble2[1] - adouble[1]).toFloat() * (Math.PI.toFloat() / 180f) * 1.5f * 5.0f
                spine.rotateAngleZ = MathHelper.rotWrap(adouble2[0] - f7.toDouble()) * (Math.PI.toFloat() / 180f) * 1.5f
                spine.rotationPointY = f3
                spine.rotationPointZ = f4
                spine.rotationPointX = f2
                f3 = (f3.toDouble() + Math.sin(spine.rotateAngleX.toDouble()) * 10.0).toFloat()
                f4 =
                    (f4.toDouble() - Math.cos(spine.rotateAngleY.toDouble()) * Math.cos(spine.rotateAngleX.toDouble()) * 10.0).toFloat()
                f2 =
                    (f2.toDouble() - Math.sin(spine.rotateAngleY.toDouble()) * Math.cos(spine.rotateAngleX.toDouble()) * 10.0).toFloat()
                spine.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn)
            }
            matrixStackIn.pop()
        }

        private fun func_229081_a_(
            p_229081_1_: MatrixStack,
            p_229081_2_: IVertexBuilder,
            p_229081_3_: Int,
            p_229081_4_: Int,
            p_229081_5_: Float,
            p_229081_6_: ModelRenderer,
            p_229081_7_: ModelRenderer,
            p_229081_8_: ModelRenderer,
            p_229081_9_: ModelRenderer,
            p_229081_10_: ModelRenderer,
            p_229081_11_: ModelRenderer,
            p_229081_12_: ModelRenderer
        )
        {
            p_229081_10_.rotateAngleX = 1.0f + p_229081_5_ * 0.1f
            p_229081_11_.rotateAngleX = 0.5f + p_229081_5_ * 0.1f
            p_229081_12_.rotateAngleX = 0.75f + p_229081_5_ * 0.1f
            p_229081_7_.rotateAngleX = 1.3f + p_229081_5_ * 0.1f
            p_229081_8_.rotateAngleX = -0.5f - p_229081_5_ * 0.1f
            p_229081_9_.rotateAngleX = 0.75f + p_229081_5_ * 0.1f
            p_229081_6_.render(p_229081_1_, p_229081_2_, p_229081_3_, p_229081_4_)
            p_229081_7_.render(p_229081_1_, p_229081_2_, p_229081_3_, p_229081_4_)
            p_229081_10_.render(p_229081_1_, p_229081_2_, p_229081_3_, p_229081_4_)
        }

        init
        {
            textureWidth = 256
            textureHeight = 256
            val f = -16.0f
            head = ModelRenderer(this)
            head.addBox("upperlip", -6.0f, -1.0f, -24.0f, 12, 5, 16, 0.0f, 176, 44)
            head.addBox("upperhead", -8.0f, -8.0f, -10.0f, 16, 16, 16, 0.0f, 112, 30)
            head.mirror = true
            head.addBox("scale", -5.0f, -12.0f, -4.0f, 2, 4, 6, 0.0f, 0, 0)
            head.addBox("nostril", -5.0f, -3.0f, -22.0f, 2, 2, 4, 0.0f, 112, 0)
            head.mirror = false
            head.addBox("scale", 3.0f, -12.0f, -4.0f, 2, 4, 6, 0.0f, 0, 0)
            head.addBox("nostril", 3.0f, -3.0f, -22.0f, 2, 2, 4, 0.0f, 112, 0)
            jaw = ModelRenderer(this)
            jaw.setRotationPoint(0.0f, 4.0f, -8.0f)
            jaw.addBox("jaw", -6.0f, 0.0f, -16.0f, 12, 4, 16, 0.0f, 176, 65)
            head.addChild(jaw)
            spine = ModelRenderer(this)
            spine.addBox("box", -5.0f, -5.0f, -5.0f, 10, 10, 10, 0.0f, 192, 104)
            spine.addBox("scale", -1.0f, -9.0f, -3.0f, 2, 4, 6, 0.0f, 48, 0)
            body = ModelRenderer(this)
            body.setRotationPoint(0.0f, 4.0f, 8.0f)
            body.addBox("body", -12.0f, 0.0f, -16.0f, 24, 24, 64, 0.0f, 0, 0)
            body.addBox("scale", -1.0f, -6.0f, -10.0f, 2, 6, 12, 0.0f, 220, 53)
            body.addBox("scale", -1.0f, -6.0f, 10.0f, 2, 6, 12, 0.0f, 220, 53)
            body.addBox("scale", -1.0f, -6.0f, 30.0f, 2, 6, 12, 0.0f, 220, 53)
            leftProximalWing = ModelRenderer(this)
            leftProximalWing.mirror = true
            leftProximalWing.setRotationPoint(12.0f, 5.0f, 2.0f)
            leftProximalWing.addBox("bone", 0.0f, -4.0f, -4.0f, 56, 8, 8, 0.0f, 112, 88)
            leftProximalWing.addBox("skin", 0.0f, 0.0f, 2.0f, 56, 0, 56, 0.0f, -56, 88)
            leftDistalWing = ModelRenderer(this)
            leftDistalWing.mirror = true
            leftDistalWing.setRotationPoint(56.0f, 0.0f, 0.0f)
            leftDistalWing.addBox("bone", 0.0f, -2.0f, -2.0f, 56, 4, 4, 0.0f, 112, 136)
            leftDistalWing.addBox("skin", 0.0f, 0.0f, 2.0f, 56, 0, 56, 0.0f, -56, 144)
            leftProximalWing.addChild(leftDistalWing)
            leftForeThigh = ModelRenderer(this)
            leftForeThigh.setRotationPoint(12.0f, 20.0f, 2.0f)
            leftForeThigh.addBox("main", -4.0f, -4.0f, -4.0f, 8, 24, 8, 0.0f, 112, 104)
            leftForeLeg = ModelRenderer(this)
            leftForeLeg.setRotationPoint(0.0f, 20.0f, -1.0f)
            leftForeLeg.addBox("main", -3.0f, -1.0f, -3.0f, 6, 24, 6, 0.0f, 226, 138)
            leftForeThigh.addChild(leftForeLeg)
            leftForeFoot = ModelRenderer(this)
            leftForeFoot.setRotationPoint(0.0f, 23.0f, 0.0f)
            leftForeFoot.addBox("main", -4.0f, 0.0f, -12.0f, 8, 4, 16, 0.0f, 144, 104)
            leftForeLeg.addChild(leftForeFoot)
            leftHindThigh = ModelRenderer(this)
            leftHindThigh.setRotationPoint(16.0f, 16.0f, 42.0f)
            leftHindThigh.addBox("main", -8.0f, -4.0f, -8.0f, 16, 32, 16, 0.0f, 0, 0)
            leftHindLeg = ModelRenderer(this)
            leftHindLeg.setRotationPoint(0.0f, 32.0f, -4.0f)
            leftHindLeg.addBox("main", -6.0f, -2.0f, 0.0f, 12, 32, 12, 0.0f, 196, 0)
            leftHindThigh.addChild(leftHindLeg)
            leftHindFoot = ModelRenderer(this)
            leftHindFoot.setRotationPoint(0.0f, 31.0f, 4.0f)
            leftHindFoot.addBox("main", -9.0f, 0.0f, -20.0f, 18, 6, 24, 0.0f, 112, 0)
            leftHindLeg.addChild(leftHindFoot)
            rightProximalWing = ModelRenderer(this)
            rightProximalWing.setRotationPoint(-12.0f, 5.0f, 2.0f)
            rightProximalWing.addBox("bone", -56.0f, -4.0f, -4.0f, 56, 8, 8, 0.0f, 112, 88)
            rightProximalWing.addBox("skin", -56.0f, 0.0f, 2.0f, 56, 0, 56, 0.0f, -56, 88)
            rightDistalWing = ModelRenderer(this)
            rightDistalWing.setRotationPoint(-56.0f, 0.0f, 0.0f)
            rightDistalWing.addBox("bone", -56.0f, -2.0f, -2.0f, 56, 4, 4, 0.0f, 112, 136)
            rightDistalWing.addBox("skin", -56.0f, 0.0f, 2.0f, 56, 0, 56, 0.0f, -56, 144)
            rightProximalWing.addChild(rightDistalWing)
            rightForeThigh = ModelRenderer(this)
            rightForeThigh.setRotationPoint(-12.0f, 20.0f, 2.0f)
            rightForeThigh.addBox("main", -4.0f, -4.0f, -4.0f, 8, 24, 8, 0.0f, 112, 104)
            rightForeLeg = ModelRenderer(this)
            rightForeLeg.setRotationPoint(0.0f, 20.0f, -1.0f)
            rightForeLeg.addBox("main", -3.0f, -1.0f, -3.0f, 6, 24, 6, 0.0f, 226, 138)
            rightForeThigh.addChild(rightForeLeg)
            rightForeFoot = ModelRenderer(this)
            rightForeFoot.setRotationPoint(0.0f, 23.0f, 0.0f)
            rightForeFoot.addBox("main", -4.0f, 0.0f, -12.0f, 8, 4, 16, 0.0f, 144, 104)
            rightForeLeg.addChild(rightForeFoot)
            rightHindThigh = ModelRenderer(this)
            rightHindThigh.setRotationPoint(-16.0f, 16.0f, 42.0f)
            rightHindThigh.addBox("main", -8.0f, -4.0f, -8.0f, 16, 32, 16, 0.0f, 0, 0)
            rightHindLeg = ModelRenderer(this)
            rightHindLeg.setRotationPoint(0.0f, 32.0f, -4.0f)
            rightHindLeg.addBox("main", -6.0f, -2.0f, 0.0f, 12, 32, 12, 0.0f, 196, 0)
            rightHindThigh.addChild(rightHindLeg)
            rightHindFoot = ModelRenderer(this)
            rightHindFoot.setRotationPoint(0.0f, 31.0f, 4.0f)
            rightHindFoot.addBox("main", -9.0f, 0.0f, -20.0f, 18, 6, 24, 0.0f, 112, 0)
            rightHindLeg.addChild(rightHindFoot)
        }
    }

    companion object
    {
        val ENDERCRYSTAL_BEAM_TEXTURES = ResourceLocation("textures/entity/end_crystal/end_crystal_beam.png")
        private val DRAGON_EXPLODING_TEXTURES = ResourceLocation("textures/entity/enderdragon/dragon_exploding.png")
        private val DRAGON_TEXTURES = ResourceLocation("textures/entity/enderdragon/dragon.png")
        private val field_229052_g_ = ResourceLocation("textures/entity/enderdragon/dragon_eyes.png")
        private val field_229053_h_ = RenderType.getEntityCutoutNoCull(DRAGON_TEXTURES)
        private val field_229054_i_ = RenderType.getEntityDecal(DRAGON_TEXTURES)
        private val field_229055_j_ = RenderType.getEyes(field_229052_g_)
        private val field_229056_k_ = RenderType.getEntitySmoothCutout(
            ENDERCRYSTAL_BEAM_TEXTURES
        )
        private val field_229057_l_ = (Math.sqrt(3.0) / 2.0).toFloat()
        private fun func_229061_a_(p_229061_0_: IVertexBuilder, p_229061_1_: Matrix4f, p_229061_2_: Int)
        {
            p_229061_0_.pos(p_229061_1_, 0.0f, 0.0f, 0.0f).color(255, 255, 255, p_229061_2_).endVertex()
            p_229061_0_.pos(p_229061_1_, 0.0f, 0.0f, 0.0f).color(255, 255, 255, p_229061_2_).endVertex()
        }

        private fun func_229060_a_(
            p_229060_0_: IVertexBuilder,
            p_229060_1_: Matrix4f,
            p_229060_2_: Float,
            p_229060_3_: Float
        )
        {
            p_229060_0_.pos(p_229060_1_, -field_229057_l_ * p_229060_3_, p_229060_2_, -0.5f * p_229060_3_)
                .color(255, 0, 255, 0).endVertex()
        }

        private fun func_229062_b_(
            p_229062_0_: IVertexBuilder,
            p_229062_1_: Matrix4f,
            p_229062_2_: Float,
            p_229062_3_: Float
        )
        {
            p_229062_0_.pos(p_229062_1_, field_229057_l_ * p_229062_3_, p_229062_2_, -0.5f * p_229062_3_)
                .color(255, 0, 255, 0).endVertex()
        }

        private fun func_229063_c_(
            p_229063_0_: IVertexBuilder,
            p_229063_1_: Matrix4f,
            p_229063_2_: Float,
            p_229063_3_: Float
        )
        {
            p_229063_0_.pos(p_229063_1_, 0.0f, p_229063_2_, 1.0f * p_229063_3_).color(255, 0, 255, 0).endVertex()
        }

        fun func_229059_a_(
            p_229059_0_: Float,
            p_229059_1_: Float,
            p_229059_2_: Float,
            p_229059_3_: Float,
            p_229059_4_: Int,
            p_229059_5_: MatrixStack,
            p_229059_6_: IRenderTypeBuffer,
            p_229059_7_: Int
        )
        {
            val f = MathHelper.sqrt(p_229059_0_ * p_229059_0_ + p_229059_2_ * p_229059_2_)
            val f1 = MathHelper.sqrt(p_229059_0_ * p_229059_0_ + p_229059_1_ * p_229059_1_ + p_229059_2_ * p_229059_2_)
            p_229059_5_.push()
            p_229059_5_.translate(0.0, 2.0, 0.0)
            p_229059_5_.rotate(
                Vector3f.YP.rotation(
                    (-Math.atan2(
                        p_229059_2_.toDouble(),
                        p_229059_0_.toDouble()
                    )).toFloat() - Math.PI.toFloat() / 2f
                )
            )
            p_229059_5_.rotate(
                Vector3f.XP.rotation(
                    (-Math.atan2(
                        f.toDouble(),
                        p_229059_1_.toDouble()
                    )).toFloat() - Math.PI.toFloat() / 2f
                )
            )
            val ivertexbuilder = p_229059_6_.getBuffer(field_229056_k_)
            val f2 = 0.0f - (p_229059_4_.toFloat() + p_229059_3_) * 0.01f
            val f3 =
                MathHelper.sqrt(p_229059_0_ * p_229059_0_ + p_229059_1_ * p_229059_1_ + p_229059_2_ * p_229059_2_) / 32.0f - (p_229059_4_.toFloat() + p_229059_3_) * 0.01f
            val i = 8
            var f4 = 0.0f
            var f5 = 0.75f
            var f6 = 0.0f
            val `matrixstack$entry` = p_229059_5_.last
            val matrix4f = `matrixstack$entry`.matrix
            val matrix3f = `matrixstack$entry`.normal
            for (j in 1..8)
            {
                val f7 = MathHelper.sin(j.toFloat() * (Math.PI.toFloat() * 2f) / 8.0f) * 0.75f
                val f8 = MathHelper.cos(j.toFloat() * (Math.PI.toFloat() * 2f) / 8.0f) * 0.75f
                val f9 = j.toFloat() / 8.0f
                ivertexbuilder.pos(matrix4f, f4 * 0.2f, f5 * 0.2f, 0.0f).color(0, 0, 0, 255).tex(f6, f2)
                    .overlay(OverlayTexture.NO_OVERLAY).lightmap(p_229059_7_).normal(matrix3f, 0.0f, -1.0f, 0.0f)
                    .endVertex()
                ivertexbuilder.pos(matrix4f, f4, f5, f1).color(255, 255, 255, 255).tex(f6, f3)
                    .overlay(OverlayTexture.NO_OVERLAY).lightmap(p_229059_7_).normal(matrix3f, 0.0f, -1.0f, 0.0f)
                    .endVertex()
                ivertexbuilder.pos(matrix4f, f7, f8, f1).color(255, 255, 255, 255).tex(f9, f3)
                    .overlay(OverlayTexture.NO_OVERLAY).lightmap(p_229059_7_).normal(matrix3f, 0.0f, -1.0f, 0.0f)
                    .endVertex()
                ivertexbuilder.pos(matrix4f, f7 * 0.2f, f8 * 0.2f, 0.0f).color(0, 0, 0, 255).tex(f9, f2)
                    .overlay(OverlayTexture.NO_OVERLAY).lightmap(p_229059_7_).normal(matrix3f, 0.0f, -1.0f, 0.0f)
                    .endVertex()
                f4 = f7
                f5 = f8
                f6 = f9
            }
            p_229059_5_.pop()
        }
    }

    init
    {
        shadowSize = 0.5f
    }
}
