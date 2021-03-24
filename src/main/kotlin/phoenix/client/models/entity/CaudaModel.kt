package phoenix.client.models.entity

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.entity.model.EntityModel
import net.minecraft.client.renderer.model.ModelRenderer
import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.MathHelper
import kotlin.math.cos

class CaudaModel<T : LivingEntity> : EntityModel<T>()
{
    private val body: ModelRenderer
    private val body2: ModelRenderer
    private val body3: ModelRenderer
    private val tail1: ModelRenderer
    private val tail2: ModelRenderer
    private val tail3: ModelRenderer
    private val clawLeft: ModelRenderer
    private val clawRight: ModelRenderer
    private val wingLeft: ModelRenderer
    private val wingLeft2: ModelRenderer
    private val wingLeft3: ModelRenderer
    private val wingLeft4: ModelRenderer
    private val wingRight: ModelRenderer
    private val wingRight2: ModelRenderer
    private val wingRight3: ModelRenderer
    private val wingRight4: ModelRenderer

    override fun setRotationAngles(entity: T, limbSwing: Float, limbSwingAmount: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float)
    {
        val ticks = ((entity.entityId * 3).toFloat() + ageInTicks) * 0.13f
        if (cos((ticks / 20).toDouble()) - 0.3 > 0)
        {
            wingLeft.rotateAngleX = MathHelper.cos(ticks) * 16.0f * (Math.PI.toFloat() / 180f)
            wingLeft2.rotateAngleX = MathHelper.cos(ticks) * 16.0f * (Math.PI.toFloat() / 180f)
            wingRight.rotateAngleX = -wingLeft.rotateAngleX
            wingRight2.rotateAngleX = -wingLeft.rotateAngleX
        } else
        {
            wingLeft.rotateAngleX = 0f
            wingLeft2.rotateAngleX = 0f
            wingRight.rotateAngleX = 0f
            wingRight2.rotateAngleX = 0f
        }
        tail3.rotateAngleZ = (5.0f + MathHelper.cos(ticks * 2.0f) * 7.0f) * (Math.PI.toFloat() / 180f)
        clawLeft.rotateAngleZ = -(5.0f + MathHelper.cos(ticks * 2.0f) * 7.0f) * (Math.PI.toFloat() / 180f)
        clawRight.rotateAngleZ = -(5.0f + MathHelper.cos(ticks * 2.0f) * 7.0f) * (Math.PI.toFloat() / 180f)
    }

    override fun render(
        matrixStack: MatrixStack,
        buffer: IVertexBuilder,
        packedLight: Int,
        packedOverlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    )
    {
        body.render(matrixStack, buffer, packedLight, packedOverlay)
        wingLeft.render(matrixStack, buffer, packedLight, packedOverlay)
        wingRight.render(matrixStack, buffer, packedLight, packedOverlay)
    }

    fun setRotationAngle(modelRenderer: ModelRenderer, x: Float, y: Float, z: Float)
    {
        modelRenderer.rotateAngleX = x
        modelRenderer.rotateAngleY = y
        modelRenderer.rotateAngleZ = z
    }

    init
    {
        textureWidth = 128
        textureHeight = 128
        body = ModelRenderer(this)
        body.setRotationPoint(1.0f, 18.0f, 0.0f)
        body.setTextureOffset(36, 0).addBox(-2.0f, -1.0f, -4.0f, 4.0f, 3.0f, 8.0f, 0.0f, false)
        body2 = ModelRenderer(this)
        body2.setRotationPoint(-2.0f, 0.0f, 0.0f)
        body.addChild(body2)
        body2.setTextureOffset(60, 9).addBox(-3.0f, 0.0f, -4.0f, 3.0f, 1.0f, 8.0f, 0.0f, false)
        body2.setTextureOffset(60, 0).addBox(-3.0f, -1.0f, -3.0f, 3.0f, 3.0f, 6.0f, 0.0f, false)
        body3 = ModelRenderer(this)
        body3.setRotationPoint(-3.0f, 0.0f, 0.0f)
        body2.addChild(body3)
        body3.setTextureOffset(12, 0).addBox(-4.0f, 0.0f, -4.0f, 4.0f, 1.0f, 8.0f, 0.0f, false)
        body3.setTextureOffset(0, 0).addBox(-4.0f, -1.0f, -2.0f, 4.0f, 3.0f, 4.0f, 0.0f, false)
        tail1 = ModelRenderer(this)
        tail1.setRotationPoint(-6.0f, 0.0f, 0.0f)
        body3.addChild(tail1)
        tail1.setTextureOffset(30, 20).addBox(-6.0f, 0.0f, 1.0f, 8.0f, 1.0f, 3.0f, 0.0f, false)
        tail1.setTextureOffset(30, 16).addBox(-6.0f, 0.0f, -4.0f, 8.0f, 1.0f, 3.0f, 0.0f, true)
        tail2 = ModelRenderer(this)
        tail2.setRotationPoint(-8.0f, 1.0f, 0.0f)
        tail1.addChild(tail2)
        tail2.setTextureOffset(30, 29).addBox(-4.0f, -1.0f, -3.0f, 6.0f, 1.0f, 2.0f, 0.0f, false)
        tail2.setTextureOffset(30, 26).addBox(-4.0f, -1.0f, 1.0f, 6.0f, 1.0f, 2.0f, 0.0f, false)
        tail3 = ModelRenderer(this)
        tail3.setRotationPoint(-3.0f, 0.0f, 0.0f)
        tail2.addChild(tail3)
        tail3.setTextureOffset(30, 24).addBox(-8.0f, -1.0f, -2.0f, 7.0f, 1.0f, 1.0f, 0.0f, false)
        tail3.setTextureOffset(30, 24).addBox(-8.0f, -1.0f, 1.0f, 7.0f, 1.0f, 1.0f, 0.0f, false)
        clawLeft = ModelRenderer(this)
        clawLeft.setRotationPoint(-4.0f, -1.0f, 2.0f)
        tail2.addChild(clawLeft)
        setRotationAngle(clawLeft, 0.0f, 0.2967f, 0.0f)
        clawLeft.setTextureOffset(30, 24).addBox(-7.0f, 0.0f, 0.0f, 7.0f, 1.0f, 1.0f, 0.0f, false)
        clawRight = ModelRenderer(this)
        clawRight.setRotationPoint(-4.0f, -1.0f, -2.0f)
        tail2.addChild(clawRight)
        setRotationAngle(clawRight, 0.0f, -0.2967f, 0.0f)
        clawRight.setTextureOffset(30, 24).addBox(-7.0f, 0.0f, -1.0f, 7.0f, 1.0f, 1.0f, 0.0f, false)
        wingLeft = ModelRenderer(this)
        wingLeft.setRotationPoint(3.0f, 16.0f, 4.0f)
        wingLeft.setTextureOffset(0, 27).addBox(-7.0f, 0.0f, 0.0f, 8.0f, 3.0f, 7.0f, 0.0f, false)
        wingLeft.setTextureOffset(52, 18).addBox(-10.0f, 1.0f, 0.0f, 3.0f, 1.0f, 7.0f, 0.0f, false)
        wingLeft2 = ModelRenderer(this)
        wingLeft2.setRotationPoint(0.0f, 1.0f, 7.0f)
        wingLeft.addChild(wingLeft2)
        setRotationAngle(wingLeft2, 0.0f, 0.0f, 0.0f)
        wingLeft2.setTextureOffset(0, 37).addBox(-7.0f, 0.0f, 0.0f, 7.0f, 2.0f, 7.0f, 0.0f, false)
        wingLeft3 = ModelRenderer(this)
        wingLeft3.setRotationPoint(0.0f, 1.0f, 7.0f)
        wingLeft2.addChild(wingLeft3)
        setRotationAngle(wingLeft3, 0.0f, 0.0f, 0.0f)
        wingLeft3.setTextureOffset(28, 32).addBox(-6.0f, 0.0f, 0.0f, 6.0f, 1.0f, 6.0f, 0.0f, false)
        wingLeft4 = ModelRenderer(this)
        wingLeft4.setRotationPoint(0.0f, 0.0f, 6.0f)
        wingLeft3.addChild(wingLeft4)
        wingLeft4.setTextureOffset(16, 9).addBox(-2.0f, 0.0f, 0.0f, 2.0f, 1.0f, 6.0f, 0.0f, false)
        wingRight = ModelRenderer(this)
        wingRight.setRotationPoint(3.0f, 16.0f, -4.0f)
        wingRight.setTextureOffset(0, 17).addBox(-7.0f, 0.0f, -7.0f, 8.0f, 3.0f, 7.0f, 0.0f, false)
        wingRight.setTextureOffset(52, 26).addBox(-10.0f, 1.0f, -7.0f, 3.0f, 1.0f, 7.0f, 0.0f, false)
        wingRight2 = ModelRenderer(this)
        wingRight2.setRotationPoint(0.0f, 1.0f, -7.0f)
        wingRight.addChild(wingRight2)
        wingRight2.setTextureOffset(0, 46).addBox(-7.0f, 0.0f, -7.0f, 7.0f, 2.0f, 7.0f, 0.0f, false)
        wingRight3 = ModelRenderer(this)
        wingRight3.setRotationPoint(0.0f, 1.0f, -7.0f)
        wingRight2.addChild(wingRight3)
        wingRight3.setTextureOffset(28, 39).addBox(-6.0f, 0.0f, -6.0f, 6.0f, 1.0f, 6.0f, 0.0f, false)
        wingRight4 = ModelRenderer(this)
        wingRight4.setRotationPoint(0.0f, 0.0f, -6.0f)
        wingRight3.addChild(wingRight4)
        wingRight4.setTextureOffset(0, 9).addBox(-2.0f, 0.0f, -6.0f, 2.0f, 1.0f, 6.0f, 0.0f, false)
    }
}