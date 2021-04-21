package phoenix.client.render

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.Quaternion
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.Vector3f
import net.minecraft.client.renderer.culling.ClippingHelperImpl
import net.minecraft.client.renderer.entity.EnderDragonRenderer
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.model.ModelRenderer
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.MathHelper
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import phoenix.enity.EnderCrystalEntity
import kotlin.math.sin

@OnlyIn(Dist.CLIENT)
class EnderCrystalRenderer(renderManager: EntityRendererManager) : EntityRenderer<EnderCrystalEntity>(renderManager)
{
    private val field_229048_g_: ModelRenderer
    private val field_229049_h_: ModelRenderer
    private val field_229050_i_: ModelRenderer

    override fun render(
        entity: EnderCrystalEntity,
        p_225623_2_: Float,
        p_225623_3_: Float,
        matrix: MatrixStack,
        p_225623_5_: IRenderTypeBuffer,
        p_225623_6_: Int
    )
    {
        matrix.push()
        val lvt_7_1_ = func_229051_a_(entity, p_225623_3_)
        val lvt_8_1_ = (entity.innerRotation.toFloat() + p_225623_3_) * 3.0f
        val lvt_9_1_ = p_225623_5_.getBuffer(rendertype)
        matrix.push()
        matrix.scale(2.0f, 2.0f, 2.0f)
        matrix.translate(0.0, -0.5, 0.0)
        val lvt_10_1_ = OverlayTexture.NO_OVERLAY
        if (entity.shouldShowBottom())
        {
            field_229050_i_.render(matrix, lvt_9_1_, p_225623_6_, lvt_10_1_)
        }
        matrix.rotate(Vector3f.YP.rotationDegrees(lvt_8_1_))
        matrix.translate(0.0, (1.5f + lvt_7_1_ / 2.0f).toDouble(), 0.0)
        matrix.rotate(Quaternion(Vector3f(field_229047_f_, 0.0f, field_229047_f_), 60.0f, true))
        field_229049_h_.render(matrix, lvt_9_1_, p_225623_6_, lvt_10_1_)
        val lvt_11_1_ = 0.875f
        matrix.scale(0.875f, 0.875f, 0.875f)
        matrix.rotate(Quaternion(Vector3f(field_229047_f_, 0.0f, field_229047_f_), 60.0f, true))
        matrix.rotate(Vector3f.YP.rotationDegrees(lvt_8_1_))
        field_229049_h_.render(matrix, lvt_9_1_, p_225623_6_, lvt_10_1_)
        matrix.scale(0.875f, 0.875f, 0.875f)
        matrix.rotate(Quaternion(Vector3f(field_229047_f_, 0.0f, field_229047_f_), 60.0f, true))
        matrix.rotate(Vector3f.YP.rotationDegrees(lvt_8_1_))
        field_229048_g_.render(matrix, lvt_9_1_, p_225623_6_, lvt_10_1_)
        matrix.pop()
        matrix.pop()
        val target = entity.getBeamTarget()
        if (target != null)
        {
            val x = target.x.toFloat() + 0.5f
            val y = target.y.toFloat() + 0.5f
            val z = target.z.toFloat() + 0.5f
            val deltaX = (x.toDouble() - entity.posX).toFloat()
            val deltaY = (y.toDouble() - entity.posY).toFloat()
            val deltaZ = (z.toDouble() - entity.posZ).toFloat()
            matrix.translate(deltaX.toDouble(), deltaY.toDouble(), deltaZ.toDouble())
            EnderDragonRenderer.func_229059_a_(-deltaX, -deltaY + lvt_7_1_, -deltaZ, p_225623_3_, entity.innerRotation, matrix, p_225623_5_, p_225623_6_)
        }
        super.render(entity, p_225623_2_, p_225623_3_, matrix, p_225623_5_, p_225623_6_)
    }

    override fun getEntityTexture(entity: EnderCrystalEntity): ResourceLocation
    {
        return ENDER_CRYSTAL_TEXTURES
    }

    override fun shouldRender(
        entity: EnderCrystalEntity,
        helper: ClippingHelperImpl,
        x: Double,
        y: Double,
        z: Double
    ): Boolean
    {
        return super.shouldRender(entity, helper, x, y, z) || entity.getBeamTarget() != null
    }

    companion object
    {
        private val ENDER_CRYSTAL_TEXTURES = ResourceLocation("textures/entity/end_crystal/end_crystal.png")
        private var rendertype: RenderType? = RenderType.getEntityCutoutNoCull(ENDER_CRYSTAL_TEXTURES)
        private var field_229047_f_ = sin(0.7853981633974483).toFloat()

        fun func_229051_a_(entity: EnderCrystalEntity, tickts: Float): Float
        {
            val rotation = entity.innerRotation.toFloat() + tickts
            var i = MathHelper.sin(rotation * 0.2f) / 2.0f + 0.5f
            i = (i * i + i) * 0.4f
            return i - 1.4f
        }
    }

    init
    {
        shadowSize = 0.5f
        field_229049_h_ = ModelRenderer(64, 32, 0, 0)
        field_229049_h_.addBox(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f)
        field_229048_g_ = ModelRenderer(64, 32, 32, 0)
        field_229048_g_.addBox(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f)
        field_229050_i_ = ModelRenderer(64, 32, 0, 16)
        field_229050_i_.addBox(-6.0f, 0.0f, -6.0f, 12.0f, 4.0f, 12.0f)
    }
}
