package phoenix.client.models.entity

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.Quaternion
import net.minecraft.client.renderer.entity.model.IHasArm
import net.minecraft.util.HandSide
import net.minecraft.util.math.MathHelper
import phoenix.client.models.KEntityModel
import phoenix.client.models.KModelRenderer
import phoenix.enity.AncientGolemEntity

class AncientGolemModel : KEntityModel<AncientGolemEntity>(), IHasArm
{
    private val head: KModelRenderer
    private val leftLeg: KModelRenderer
    private val rightLeg: KModelRenderer
    private val south: KModelRenderer
    private val north: KModelRenderer
    private val top: KModelRenderer
    private val west: KModelRenderer
    private val east: KModelRenderer

    override fun setRotationAngles(
        entity: AncientGolemEntity,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float,
        partialTick : Float
    )
    {
        this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount
        this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + Math.PI.toFloat()) * 1.4f * limbSwingAmount
        top.yOffset = entity.breathState
        north.zOffset = entity.breathState
        south.zOffset = -entity.breathState
        east.xOffset = entity.breathState
        west.xOffset = -entity.breathState
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
        head.render(matrixStack, buffer, packedLight, packedOverlay)
        leftLeg.render(matrixStack, buffer, packedLight, packedOverlay)
        rightLeg.render(matrixStack, buffer, packedLight, packedOverlay)
        top.render(matrixStack, buffer, packedLight, packedOverlay)
        north.render(matrixStack, buffer, packedLight, packedOverlay)
        south.render(matrixStack, buffer, packedLight, packedOverlay)
        east.render(matrixStack, buffer, packedLight, packedOverlay)
        west.render(matrixStack, buffer, packedLight, packedOverlay)
    }

    init
    {

        textureWidth = 64
        textureHeight = 64

        head = KModelRenderer(this)
        head.setRotationPoint(0.0f, 15.0f, 0.0f)
        head.setTextureOffset(22, 0).addBox(-3.0f, -3.0f, -3.0f, 6.0f, 6.0f, 6.0f, 0.0f, false)

        leftLeg = KModelRenderer(this)
        leftLeg.setRotationPoint(3.0f, 18.0f, 0.0f)
        leftLeg.setTextureOffset(22, 12).addBox(-1.0f, 0.0f, -1.0f, 2.0f, 6.0f, 2.0f, 0.0f, false)

        rightLeg = KModelRenderer(this)
        rightLeg.setRotationPoint(-3.0f, 18.0f, 0.0f)
        rightLeg.setTextureOffset(30, 12).addBox(-1.0f, 0.0f, -1.0f, 2.0f, 6.0f, 2.0f, 0.0f, false)

        top = KModelRenderer(this)
        top.setRotationPoint(0.0f, 16.0f, 0.0f)
        top.setTextureOffset(22, 20).addBox(-5.0f, -6.0f, -5.0f, 10.0f, 1.0f, 10.0f, 0.0f, false)

        north = KModelRenderer(this)
        north.setRotationPoint(0.0f, 16.0f, 0.0f)
        north.setTextureOffset(0, 11).addBox(-5.0f, -5.0f, -6.0f, 10.0f, 10.0f, 1.0f, 0.0f, false)

        south = KModelRenderer(this)
        south.setRotationPoint(0.0f, 16.0f, 0.0f)
        south.setTextureOffset(0, 0).addBox(-5.0f, -5.0f, 5.0f, 10.0f, 10.0f, 1.0f, 0.0f, false)

        east = KModelRenderer(this)
        east.setRotationPoint(0.0f, 16.0f, 0.0f)
        east.setTextureOffset(0, 42).addBox(-6.0f, -5.0f, -5.0f, 1.0f, 10.0f, 10.0f, 0.0f, false)

        west = KModelRenderer(this)
        west.setRotationPoint(0.0f, 16.0f, 0.0f)
        west.setTextureOffset(0, 22).addBox(5.0f, -5.0f, -5.0f, 1.0f, 10.0f, 10.0f, 0.0f, false)
    }

    override fun translateHand(sideIn: HandSide, matrixStackIn: MatrixStack)
    {
        head.translateRotate(matrixStackIn)
        matrixStackIn.translate(0.0, -0.5, 0.0)
        matrixStackIn.rotate(Quaternion(0f, 45f, 0f, true))
        matrixStackIn.scale(0.5f, 0.5f, 0.5f)
    }
}