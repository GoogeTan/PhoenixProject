package phoenix.client.models.entity

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.entity.model.EntityModel
import net.minecraft.client.renderer.model.ModelRenderer
import phoenix.enity.projectile.IceBallEntity
import kotlin.math.sin

class IceBallModel : EntityModel<IceBallEntity>()
{
    private val ball: ModelRenderer
    override fun setRotationAngles(
        entity: IceBallEntity,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    )
    {
        ball.rotateAngleY += sin(limbSwing * limbSwingAmount * 1.2f)
        ball.rotateAngleX += sin(limbSwing * limbSwingAmount * 1.5f)
    }

    override fun render
        (
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
        ball.render(matrixStack, buffer, packedLight, packedOverlay)
    }

    init
    {
        textureWidth = 16
        textureHeight = 16
        ball = ModelRenderer(this)
        ball.setRotationPoint(0.0f, 24.0f, 0.0f)
        ball.setTextureOffset(0, 6).addBox(-3.5f, -3.5f, -0.5f, 4.0f, 4.0f, 4.0f, 0.0f, false)
        ball.setTextureOffset(0, 0).addBox(-3.0f, -3.0f, 0.0f, 3.0f, 3.0f, 3.0f, 0.0f, false)
    }
}