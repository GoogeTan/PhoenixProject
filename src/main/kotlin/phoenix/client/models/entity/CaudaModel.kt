package phoenix.client.models.entity

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.entity.model.EntityModel
import net.minecraft.client.renderer.model.ModelRenderer
import phoenix.enity.CaudaEntity

class CaudaModel : EntityModel<CaudaEntity>()
{
    private val body: ModelRenderer
    private val head: ModelRenderer
    private val jaw: ModelRenderer
    private val wingLT: ModelRenderer
    private val wingLT2: ModelRenderer
    private val wingLT3: ModelRenderer
    private val wingLB: ModelRenderer
    private val wingLB2: ModelRenderer
    private val wingLB3: ModelRenderer
    private val wingRT: ModelRenderer
    private val wingRT2: ModelRenderer
    private val wingRT3: ModelRenderer
    private val wingRB: ModelRenderer
    private val wingRB2: ModelRenderer
    private val wingRB3: ModelRenderer

    override fun setRotationAngles(entity: CaudaEntity, limbSwing: Float, limbSwingAmount: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float)
    {
        //previously the render function, render code was moved to a method below
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
        head.render(matrixStack, buffer, packedLight, packedOverlay)
        wingLT.render(matrixStack, buffer, packedLight, packedOverlay)
        wingLB.render(matrixStack, buffer, packedLight, packedOverlay)
        wingRT.render(matrixStack, buffer, packedLight, packedOverlay)
        wingRB.render(matrixStack, buffer, packedLight, packedOverlay)
    }

    private fun setRotationAngle(modelRenderer: ModelRenderer, x: Float, y: Float, z: Float)
    {
        modelRenderer.rotateAngleX = x
        modelRenderer.rotateAngleY = y
        modelRenderer.rotateAngleZ = z
    }

    init
    {
        textureWidth = 64
        textureHeight = 64
        body = ModelRenderer(this)
        body.setRotationPoint(0.0f, 24.0f, -3.0f)
        body.setTextureOffset(0, 0).addBox(-3.0f, -3.0f, 0.0f, 6.0f, 3.0f, 7.0f, 0.0f, false)
        body.setTextureOffset(0, 10).addBox(-2.0f, -3.5f, 1.0f, 4.0f, 1.0f, 5.0f, 0.0f, false)
        head = ModelRenderer(this)
        head.setRotationPoint(0.0f, 24.0f, 0.0f)
        head.setTextureOffset(18, 10).addBox(-1.999f, -2.0f, -4.055f, 4.0f, 2.0f, 1.0f, 0.0f, false)
        jaw = ModelRenderer(this)
        jaw.setRotationPoint(0.0f, 0.0f, 4.0f)
        head.addChild(jaw)
        setRotationAngle(jaw, 0.0f, 0.0f, 0.0f)
        jaw.setTextureOffset(18, 13).addBox(-2.0f, -1.05f, -8.05f, 4.0f, 1.0f, 1.0f, 0.0f, false)
        wingLT = ModelRenderer(this)
        wingLT.setRotationPoint(3.0f, 22.0f, -4.0f)
        wingLT.setTextureOffset(0, 24).addBox(0.0f, 0.0f, 1.0f, 3.0f, 1.0f, 3.0f, 0.0f, false)
        wingLT2 = ModelRenderer(this)
        wingLT2.setRotationPoint(3.0f, 0.0f, 0.0f)
        wingLT.addChild(wingLT2)
        wingLT2.setTextureOffset(12, 25).addBox(0.0f, 0.0f, 1.0f, 5.0f, 1.0f, 2.0f, 0.0f, false)
        wingLT3 = ModelRenderer(this)
        wingLT3.setRotationPoint(5.0f, 0.0f, -3.0f)
        wingLT2.addChild(wingLT3)
        wingLT3.setTextureOffset(26, 26).addBox(0.0f, 0.0f, 4.0f, 6.0f, 1.0f, 1.0f, 0.0f, false)
        wingLB = ModelRenderer(this)
        wingLB.setRotationPoint(3.0f, 21.0f, -1.0f)
        wingLB.setTextureOffset(0, 28).addBox(0.0f, 0.0f, 1.0f, 3.0f, 1.0f, 3.0f, 0.0f, false)
        wingLB2 = ModelRenderer(this)
        wingLB2.setRotationPoint(3.0f, 0.0f, 0.0f)
        wingLB.addChild(wingLB2)
        wingLB2.setTextureOffset(12, 29).addBox(0.0f, 0.0f, 1.0f, 5.0f, 1.0f, 2.0f, 0.0f, false)
        wingLB3 = ModelRenderer(this)
        wingLB3.setRotationPoint(5.0f, 0.0f, -3.0f)
        wingLB2.addChild(wingLB3)
        wingLB3.setTextureOffset(26, 30).addBox(0.0f, 0.0f, 4.0f, 6.0f, 1.0f, 1.0f, 0.0f, false)
        wingRT = ModelRenderer(this)
        wingRT.setRotationPoint(-3.0f, 22.0f, -4.0f)
        wingRT.setTextureOffset(0, 16).addBox(-3.0f, 0.0f, 1.0f, 3.0f, 1.0f, 3.0f, 0.0f, false)
        wingRT2 = ModelRenderer(this)
        wingRT2.setRotationPoint(-3.0f, 0.0f, 0.0f)
        wingRT.addChild(wingRT2)
        wingRT2.setTextureOffset(12, 17).addBox(-5.0f, 0.0f, 1.0f, 5.0f, 1.0f, 2.0f, 0.0f, false)
        wingRT3 = ModelRenderer(this)
        wingRT3.setRotationPoint(-5.0f, 0.0f, -3.0f)
        wingRT2.addChild(wingRT3)
        wingRT3.setTextureOffset(26, 18).addBox(-6.0f, 0.0f, 4.0f, 6.0f, 1.0f, 1.0f, 0.0f, false)
        wingRB = ModelRenderer(this)
        wingRB.setRotationPoint(-3.0f, 21.0f, -1.0f)
        wingRB.setTextureOffset(0, 20).addBox(-3.0f, 0.0f, 1.0f, 3.0f, 1.0f, 3.0f, 0.0f, false)
        wingRB2 = ModelRenderer(this)
        wingRB2.setRotationPoint(-3.0f, 0.0f, 0.0f)
        wingRB.addChild(wingRB2)
        wingRB2.setTextureOffset(12, 21).addBox(-5.0f, 0.0f, 1.0f, 5.0f, 1.0f, 2.0f, 0.0f, true)
        wingRB3 = ModelRenderer(this)
        wingRB3.setRotationPoint(-5.0f, 0.0f, -3.0f)
        wingRB2.addChild(wingRB3)
        wingRB3.setTextureOffset(26, 22).addBox(-6.0f, 0.0f, 4.0f, 6.0f, 1.0f, 1.0f, 0.0f, false)
    }
}