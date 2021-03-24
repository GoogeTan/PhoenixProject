package phoenix.client.models.entity

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.entity.model.EntityModel
import net.minecraft.client.renderer.model.ModelRenderer
import net.minecraft.entity.Entity

class Cauda2Model<T : Entity> : EntityModel<T>()
{
    private val body: ModelRenderer
    private val legR1: ModelRenderer
    private val legR2: ModelRenderer
    private val legL1: ModelRenderer
    private val legL2: ModelRenderer
    private val head: ModelRenderer
    private val cube_r1: ModelRenderer
    private val body2: ModelRenderer
    private val tailL: ModelRenderer
    private val tailL2: ModelRenderer
    private val clawLL: ModelRenderer
    private val clawLL2: ModelRenderer
    private val clawLL3: ModelRenderer
    private val clawLM: ModelRenderer
    private val clawLM2: ModelRenderer
    private val clawLM3: ModelRenderer
    private val clawLR: ModelRenderer
    private val clawLR1: ModelRenderer
    private val clawLR2: ModelRenderer
    private val tailM: ModelRenderer
    private val tailM2: ModelRenderer
    private val clawML: ModelRenderer
    private val clawML1: ModelRenderer
    private val clawML2: ModelRenderer
    private val clawMM: ModelRenderer
    private val clawML4: ModelRenderer
    private val clawML5: ModelRenderer
    private val clawMR: ModelRenderer
    private val clawMR2: ModelRenderer
    private val clawMR3: ModelRenderer
    private val tailR: ModelRenderer
    private val tailL4: ModelRenderer
    private val clawLL4: ModelRenderer
    private val clawLL5: ModelRenderer
    private val clawLL6: ModelRenderer
    private val clawLM4: ModelRenderer
    private val clawLM5: ModelRenderer
    private val clawLM6: ModelRenderer
    private val clawLR3: ModelRenderer
    private val clawLR4: ModelRenderer
    private val clawLR5: ModelRenderer
    private val wingL: ModelRenderer
    private val wilgL2: ModelRenderer
    private val wildL3: ModelRenderer
    private val wingL4: ModelRenderer
    private val wingL5: ModelRenderer
    private val wingR: ModelRenderer
    private val wingR2: ModelRenderer
    private val wingR3: ModelRenderer
    private val wingR4: ModelRenderer
    private val wingR5: ModelRenderer

    override fun setRotationAngles(entityIn: T, limbSwing: Float, limbSwingAmount: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float)
    {
        //previously the render function, render code was moved to a method below
    }

    override fun render(matrixStack: MatrixStack, buffer: IVertexBuilder, packedLight: Int, packedOverlay: Int, red: Float, green: Float, blue: Float, alpha: Float)
    {
        body.render(matrixStack, buffer, packedLight, packedOverlay)
        wingL.render(matrixStack, buffer, packedLight, packedOverlay)
        wingR.render(matrixStack, buffer, packedLight, packedOverlay)
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
        body.setRotationPoint(0.0f, 19.0f, -5.0f)
        body.setTextureOffset(0, 0).addBox(-3.0f, -2.0f, -3.0f, 6.0f, 3.0f, 9.0f, 0.0f, false)
        body.setTextureOffset(0, 0).addBox(1.0f, -1.0f, 6.0f, 2.0f, 1.0f, 3.0f, 0.0f, false)
        body.setTextureOffset(0, 0).addBox(-3.0f, -1.0f, 6.0f, 2.0f, 1.0f, 3.0f, 0.0f, false)
        body.setTextureOffset(0, 12).addBox(-1.0f, -2.0f, 6.0f, 2.0f, 3.0f, 3.0f, 0.0f, false)
        legR1 = ModelRenderer(this)
        legR1.setRotationPoint(-3.0f, 1.0f, -3.0f)
        body.addChild(legR1)
        legR1.setTextureOffset(10, 12).addBox(0.0f, 0.0f, -1.0f, 1.0f, 2.0f, 1.0f, 0.0f, false)
        legR2 = ModelRenderer(this)
        legR2.setRotationPoint(1.0f, 2.0f, -1.0f)
        legR1.addChild(legR2)
        legR2.setTextureOffset(10, 15).addBox(-1.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f, 0.0f, false)
        legL1 = ModelRenderer(this)
        legL1.setRotationPoint(3.0f, 1.0f, -3.0f)
        body.addChild(legL1)
        legL1.setTextureOffset(10, 12).addBox(-1.0f, 0.0f, -1.0f, 1.0f, 2.0f, 1.0f, 0.0f, false)
        legL2 = ModelRenderer(this)
        legL2.setRotationPoint(-1.0f, 2.0f, -1.0f)
        legL1.addChild(legL2)
        legL2.setTextureOffset(10, 15).addBox(0.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f, 0.0f, false)
        head = ModelRenderer(this)
        head.setRotationPoint(0.0f, -2.0f, -3.0f)
        body.addChild(head)
        head.setTextureOffset(14, 14).addBox(-3.0f, 0.0f, -3.0f, 6.0f, 2.0f, 3.0f, 0.0f, false)
        head.setTextureOffset(21, 0).addBox(-2.0f, 0.0f, -5.0f, 4.0f, 1.0f, 2.0f, 0.0f, false)
        head.setTextureOffset(21, 3).addBox(-2.0f, 1.0f, -5.0f, 4.0f, 1.0f, 2.0f, 0.0f, false)
        cube_r1 = ModelRenderer(this)
        cube_r1.setRotationPoint(0.0f, 0.0f, 0.0f)
        head.addChild(cube_r1)
        setRotationAngle(cube_r1, -1.0472f, 0.0f, 0.0f)
        cube_r1.setTextureOffset(14, 12).addBox(-3.0f, -2.0f, 0.0f, 6.0f, 2.0f, 0.0f, 0.0f, false)
        body2 = ModelRenderer(this)
        body2.setRotationPoint(0.0f, 0.0f, 9.0f)
        body.addChild(body2)
        body2.setTextureOffset(0, 0).addBox(-1.0f, -1.0f, 0.0f, 2.0f, 1.0f, 3.0f, 0.0f, false)
        tailL = ModelRenderer(this)
        tailL.setRotationPoint(2.0f, -1.0f, 0.0f)
        body2.addChild(tailL)
        setRotationAngle(tailL, 0.0f, 0.2618f, 0.0f)
        tailL.setTextureOffset(0, 45).addBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 11.0f, 0.0f, false)
        tailL.setTextureOffset(0, 57).addBox(-1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 8.0f, 0.0f, false)
        tailL2 = ModelRenderer(this)
        tailL2.setRotationPoint(0.0f, 0.0f, 11.0f)
        tailL.addChild(tailL2)
        tailL2.setTextureOffset(0, 66).addBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 4.0f, 0.0f, false)
        clawLL = ModelRenderer(this)
        clawLL.setRotationPoint(1.0f, 0.0f, 4.0f)
        tailL2.addChild(clawLL)
        setRotationAngle(clawLL, 0.0f, 0.384f, 0.0f)
        clawLL.setTextureOffset(0, 45).addBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        clawLL2 = ModelRenderer(this)
        clawLL2.setRotationPoint(0.0f, 0.0f, 2.0f)
        clawLL.addChild(clawLL2)
        clawLL2.setTextureOffset(0, 48).addBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        clawLL3 = ModelRenderer(this)
        clawLL3.setRotationPoint(0.0f, 0.0f, 2.0f)
        clawLL2.addChild(clawLL3)
        clawLL3.setTextureOffset(0, 51).addBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        clawLM = ModelRenderer(this)
        clawLM.setRotationPoint(0.0f, 0.0f, 4.0f)
        tailL2.addChild(clawLM)
        clawLM.setTextureOffset(13, 45).addBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        clawLM2 = ModelRenderer(this)
        clawLM2.setRotationPoint(0.0f, 0.0f, 2.0f)
        clawLM.addChild(clawLM2)
        clawLM2.setTextureOffset(13, 48).addBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        clawLM3 = ModelRenderer(this)
        clawLM3.setRotationPoint(0.0f, 0.0f, 2.0f)
        clawLM2.addChild(clawLM3)
        clawLM3.setTextureOffset(13, 51).addBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        clawLR = ModelRenderer(this)
        clawLR.setRotationPoint(0.0f, 0.0f, 4.0f)
        tailL2.addChild(clawLR)
        setRotationAngle(clawLR, 0.0f, -0.384f, 0.0f)
        clawLR.setTextureOffset(20, 45).addBox(-1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        clawLR1 = ModelRenderer(this)
        clawLR1.setRotationPoint(0.0f, 0.0f, 2.0f)
        clawLR.addChild(clawLR1)
        clawLR1.setTextureOffset(20, 48).addBox(-1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        clawLR2 = ModelRenderer(this)
        clawLR2.setRotationPoint(0.0f, 0.0f, 2.0f)
        clawLR1.addChild(clawLR2)
        clawLR2.setTextureOffset(20, 51).addBox(-1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        tailM = ModelRenderer(this)
        tailM.setRotationPoint(0.0f, -1.0f, 3.0f)
        body2.addChild(tailM)
        tailM.setTextureOffset(26, 45).addBox(-1.0f, 0.0f, 0.0f, 2.0f, 1.0f, 9.0f, 0.0f, false)
        tailM2 = ModelRenderer(this)
        tailM2.setRotationPoint(0.0f, 0.0f, 9.0f)
        tailM.addChild(tailM2)
        tailM2.setTextureOffset(26, 55).addBox(-0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 9.0f, 0.0f, false)
        clawML = ModelRenderer(this)
        clawML.setRotationPoint(0.5f, 0.0f, 9.0f)
        tailM2.addChild(clawML)
        setRotationAngle(clawML, 0.0f, 0.3491f, 0.0f)
        clawML.setTextureOffset(26, 45).addBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        clawML1 = ModelRenderer(this)
        clawML1.setRotationPoint(0.0f, 0.0f, 2.0f)
        clawML.addChild(clawML1)
        clawML1.setTextureOffset(26, 48).addBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        clawML2 = ModelRenderer(this)
        clawML2.setRotationPoint(0.0f, 0.0f, 2.0f)
        clawML1.addChild(clawML2)
        clawML2.setTextureOffset(26, 51).addBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        clawMM = ModelRenderer(this)
        clawMM.setRotationPoint(-0.5f, 0.0f, 9.0f)
        tailM2.addChild(clawMM)
        clawMM.setTextureOffset(26, 55).addBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        clawML4 = ModelRenderer(this)
        clawML4.setRotationPoint(0.0f, 0.0f, 2.0f)
        clawMM.addChild(clawML4)
        clawML4.setTextureOffset(26, 58).addBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        clawML5 = ModelRenderer(this)
        clawML5.setRotationPoint(0.0f, 0.0f, 2.0f)
        clawML4.addChild(clawML5)
        clawML5.setTextureOffset(26, 61).addBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        clawMR = ModelRenderer(this)
        clawMR.setRotationPoint(-0.5f, 0.0f, 9.0f)
        tailM2.addChild(clawMR)
        setRotationAngle(clawMR, 0.0f, -0.3491f, 0.0f)
        clawMR.setTextureOffset(38, 55).addBox(-1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        clawMR2 = ModelRenderer(this)
        clawMR2.setRotationPoint(0.0f, 0.0f, 2.0f)
        clawMR.addChild(clawMR2)
        clawMR2.setTextureOffset(38, 58).addBox(-1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        clawMR3 = ModelRenderer(this)
        clawMR3.setRotationPoint(0.0f, 0.0f, 2.0f)
        clawMR2.addChild(clawMR3)
        clawMR3.setTextureOffset(38, 61).addBox(-1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        tailR = ModelRenderer(this)
        tailR.setRotationPoint(-2.0f, -1.0f, 0.0f)
        body2.addChild(tailR)
        setRotationAngle(tailR, 0.0f, -0.2618f, 0.0f)
        tailR.setTextureOffset(49, 35).addBox(-1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 11.0f, 0.0f, false)
        tailR.setTextureOffset(49, 47).addBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 8.0f, 0.0f, false)
        tailL4 = ModelRenderer(this)
        tailL4.setRotationPoint(0.0f, 0.0f, 11.0f)
        tailR.addChild(tailL4)
        tailL4.setTextureOffset(62, 47).addBox(-1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 4.0f, 0.0f, false)
        clawLL4 = ModelRenderer(this)
        clawLL4.setRotationPoint(0.0f, 0.0f, 4.0f)
        tailL4.addChild(clawLL4)
        setRotationAngle(clawLL4, 0.0f, 0.384f, 0.0f)
        clawLL4.setTextureOffset(49, 65).addBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        clawLL5 = ModelRenderer(this)
        clawLL5.setRotationPoint(0.0f, 0.0f, 2.0f)
        clawLL4.addChild(clawLL5)
        clawLL5.setTextureOffset(49, 68).addBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        clawLL6 = ModelRenderer(this)
        clawLL6.setRotationPoint(0.0f, 0.0f, 2.0f)
        clawLL5.addChild(clawLL6)
        clawLL6.setTextureOffset(49, 71).addBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        clawLM4 = ModelRenderer(this)
        clawLM4.setRotationPoint(-1.0f, 0.0f, 4.0f)
        tailL4.addChild(clawLM4)
        clawLM4.setTextureOffset(49, 56).addBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        clawLM5 = ModelRenderer(this)
        clawLM5.setRotationPoint(0.0f, 0.0f, 2.0f)
        clawLM4.addChild(clawLM5)
        clawLM5.setTextureOffset(49, 59).addBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        clawLM6 = ModelRenderer(this)
        clawLM6.setRotationPoint(0.0f, 0.0f, 2.0f)
        clawLM5.addChild(clawLM6)
        clawLM6.setTextureOffset(49, 62).addBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        clawLR3 = ModelRenderer(this)
        clawLR3.setRotationPoint(-1.0f, 0.0f, 4.0f)
        tailL4.addChild(clawLR3)
        setRotationAngle(clawLR3, 0.0f, -0.384f, 0.0f)
        clawLR3.setTextureOffset(55, 56).addBox(-1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        clawLR4 = ModelRenderer(this)
        clawLR4.setRotationPoint(0.0f, 0.0f, 2.0f)
        clawLR3.addChild(clawLR4)
        clawLR4.setTextureOffset(55, 59).addBox(-1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        clawLR5 = ModelRenderer(this)
        clawLR5.setRotationPoint(0.0f, 0.0f, 2.0f)
        clawLR4.addChild(clawLR5)
        clawLR5.setTextureOffset(55, 62).addBox(-1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        wingL = ModelRenderer(this)
        wingL.setRotationPoint(-3.0f, 17.0f, -8.0f)
        wingL.setTextureOffset(33, 6).addBox(-8.0f, 1.0f, 3.0f, 8.0f, 1.0f, 4.0f, 0.0f, false)
        wingL.setTextureOffset(33, 11).addBox(-8.0f, 1.0f, 7.0f, 8.0f, 0.0f, 3.0f, 0.0f, false)
        wingL.setTextureOffset(33, 0).addBox(-8.0f, 0.0f, 0.0f, 8.0f, 3.0f, 3.0f, 0.0f, false)
        wilgL2 = ModelRenderer(this)
        wilgL2.setRotationPoint(-8.0f, 1.0f, 0.0f)
        wingL.addChild(wilgL2)
        wilgL2.setTextureOffset(33, 23).addBox(-9.0f, 0.0f, 6.0f, 9.0f, 0.0f, 3.0f, 0.0f, false)
        wilgL2.setTextureOffset(32, 14).addBox(-9.0f, 0.0f, 0.0f, 9.0f, 2.0f, 2.0f, 0.0f, false)
        wilgL2.setTextureOffset(32, 18).addBox(-9.0f, 0.0f, 2.0f, 9.0f, 1.0f, 4.0f, 0.0f, false)
        wildL3 = ModelRenderer(this)
        wildL3.setRotationPoint(-9.0f, 0.0f, 0.0f)
        wilgL2.addChild(wildL3)
        wildL3.setTextureOffset(58, 0).addBox(-9.0f, 0.0f, 0.0f, 9.0f, 1.0f, 4.0f, 0.0f, false)
        wildL3.setTextureOffset(58, 5).addBox(-9.0f, 0.0f, 4.0f, 9.0f, 0.0f, 3.0f, 0.0f, false)
        wingL4 = ModelRenderer(this)
        wingL4.setRotationPoint(-9.0f, 0.0f, 0.0f)
        wildL3.addChild(wingL4)
        wingL4.setTextureOffset(57, 8).addBox(-5.0f, 0.0f, 0.0f, 5.0f, 1.0f, 2.0f, 0.0f, false)
        wingL4.setTextureOffset(58, 13).addBox(-5.0f, 0.0f, 2.0f, 5.0f, 0.0f, 3.0f, 0.0f, false)
        wingL5 = ModelRenderer(this)
        wingL5.setRotationPoint(0.0f, 0.0f, 0.0f)
        wingL4.addChild(wingL5)
        wingL5.setTextureOffset(57, 11).addBox(-11.0f, 0.0f, 0.0f, 6.0f, 0.0f, 2.0f, 0.0f, false)
        wingR = ModelRenderer(this)
        wingR.setRotationPoint(3.0f, 17.0f, -8.0f)
        wingR.setTextureOffset(0, 25).addBox(0.0f, 1.0f, 3.0f, 8.0f, 1.0f, 4.0f, 0.0f, false)
        wingR.setTextureOffset(0, 19).addBox(0.0f, 0.0f, 0.0f, 8.0f, 3.0f, 3.0f, 0.0f, false)
        wingR.setTextureOffset(0, 30).addBox(0.0f, 1.0f, 7.0f, 8.0f, 0.0f, 3.0f, 0.0f, false)
        wingR2 = ModelRenderer(this)
        wingR2.setRotationPoint(13.0f, 1.0f, 2.0f)
        wingR.addChild(wingR2)
        wingR2.setTextureOffset(0, 42).addBox(-5.0f, 0.0f, 4.0f, 9.0f, 0.0f, 3.0f, 0.0f, false)
        wingR2.setTextureOffset(0, 33).addBox(-5.0f, 0.0f, -2.0f, 9.0f, 2.0f, 2.0f, 0.0f, false)
        wingR2.setTextureOffset(0, 37).addBox(-5.0f, 0.0f, 0.0f, 9.0f, 1.0f, 4.0f, 0.0f, false)
        wingR3 = ModelRenderer(this)
        wingR3.setRotationPoint(4.0f, 0.0f, -2.0f)
        wingR2.addChild(wingR3)
        wingR3.setTextureOffset(26, 29).addBox(0.0f, 0.0f, 0.0f, 9.0f, 1.0f, 4.0f, 0.0f, false)
        wingR3.setTextureOffset(27, 26).addBox(0.0f, 0.0f, 4.0f, 9.0f, 0.0f, 3.0f, 0.0f, false)
        wingR4 = ModelRenderer(this)
        wingR4.setRotationPoint(9.0f, 0.0f, 0.0f)
        wingR3.addChild(wingR4)
        wingR4.setTextureOffset(26, 34).addBox(0.0f, 0.0f, 0.0f, 5.0f, 1.0f, 2.0f, 0.0f, false)
        wingR4.setTextureOffset(26, 37).addBox(0.0f, 0.0f, 2.0f, 5.0f, 0.0f, 3.0f, 0.0f, false)
        wingR5 = ModelRenderer(this)
        wingR5.setRotationPoint(5.0f, 0.0f, 0.0f)
        wingR4.addChild(wingR5)
        wingR5.setTextureOffset(26, 40).addBox(0.0f, 0.0f, 0.0f, 6.0f, 0.0f, 2.0f, 0.0f, false)
    }
}