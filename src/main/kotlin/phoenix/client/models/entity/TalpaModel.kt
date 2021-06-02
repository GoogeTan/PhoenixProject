package phoenix.client.models.entity

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.entity.model.EntityModel
import net.minecraft.client.renderer.model.ModelRenderer
import phoenix.enity.TalpaEntity
import kotlin.math.cos


class TalpaModel : EntityModel<TalpaEntity>()
{
    lateinit var body: ModelRenderer
    var paws = arrayOf(ModelRenderer(this, 0, 8), ModelRenderer(this, 0, 8), ModelRenderer(this, 0, 8), ModelRenderer(this, 0, 8))
    override fun setLivingAnimations(entityIn: TalpaEntity, limbSwing: Float, limbSwingAmount: Float, partialTick: Float)
    {
        if (isChild)
        {
            textureHeight = 16
            textureWidth = 16
            body = ModelRenderer(this, 0, 0)
            body.addBox(0f, 0f, 0f, 3f, 3f, 5f)
            body.setRotationPoint(-1f, 18f, -1f)
            paws[0].addBox(0f, 0f, 0f, 1f, 1f, 3f)
            paws[1].addBox(0f, 0f, 0f, 1f, 1f, 3f)
            paws[2].addBox(0f, 0f, 0f, 1f, 1f, 3f)
            paws[3].addBox(0f, 0f, 0f, 1f, 1f, 3f)
        } else
        {
            textureHeight = 32
            textureWidth = 32
            body = ModelRenderer(this, 0, 0)
            body.addBox(0f, 0f, 0f, 6f, 6f, 10f)
            body.setRotationPoint(-2f, 18f, -2f)
            paws[0] = ModelRenderer(this, 0, 16)
            paws[1] = ModelRenderer(this, 0, 16)
            paws[2] = ModelRenderer(this, 0, 16)
            paws[3] = ModelRenderer(this, 0, 16)
            paws[0].addBox(0f, 0f, 0f, 1f, 2f, 6f)
            paws[1].addBox(0f, 0f, 0f, 1f, 2f, 6f)
            paws[2].addBox(0f, 0f, 0f, 2f, 1f, 6f)
            paws[3].addBox(0f, 0f, 0f, 2f, 1f, 6f)
        }

        paws[0].setRotationPoint(+1f + 3, -2f + 2 + 20, +1 + cos(limbSwing * 0.6662f) * 4f * limbSwingAmount)
        paws[1].setRotationPoint(-5f + 2, -2f + 2 + 20, +1 + cos(limbSwing * 0.6662f) * 4f * limbSwingAmount)
        paws[2].setRotationPoint(-2f + 2, +1f + 3 + 20, -5 + cos(limbSwing * 0.6662f + Math.PI.toFloat()) * 4f * limbSwingAmount)
        paws[3].setRotationPoint(-2f + 2, -5f + 2 + 20, -5 + cos(limbSwing * 0.6662f + Math.PI.toFloat()) * 4f * limbSwingAmount)
        if (entityIn.isChild)
        {
            for (part in paws)
            {
                part.rotationPointX /= 2f
                part.rotationPointZ /= 2f
            }
            paws[0].rotationPointY -= 1f
            paws[1].rotationPointY -= 1f
            paws[2].rotationPointY -= 3f
        }
    }

    override fun setRotationAngles(
        entityIn: TalpaEntity,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    )
    {
        paws[0].rotateAngleY = -25f
        paws[1].rotateAngleY = 25f
        paws[2].rotateAngleX = -25f
        paws[3].rotateAngleX = 25f
    }

    override fun render(matrixStackIn: MatrixStack, bufferIn: IVertexBuilder, packedLightIn: Int, packedOverlayIn: Int, red: Float, green: Float, blue: Float, alpha: Float)
    {
        body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha)
        for (paw in paws)
            paw.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha)
    }
}
