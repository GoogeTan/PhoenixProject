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
    private val cube2: ModelRenderer
    private val cube1: ModelRenderer
    private val base : ModelRenderer

    override fun render(
        entity: EnderCrystalEntity,
        entityYaw: Float,
        partialTicks: Float,
        matrix: MatrixStack,
        bufferIn: IRenderTypeBuffer,
        packedLightIn: Int
    )
    {
        matrix.push()
        val lvt_7_1_ = func_229051_a_(entity, partialTicks)
        val rotation = (entity.innerRotation.toFloat() + partialTicks) * 3.0f
        val builder = bufferIn.getBuffer(rendertype)
        matrix.push()
        matrix.scale(2.0f, 2.0f, 2.0f)
        matrix.translate(0.0, -0.5, 0.0)
        val noOverlay = OverlayTexture.NO_OVERLAY
        if (entity.shouldShowBottom())
        {
            base.render(matrix, builder, packedLightIn, noOverlay)
        }
        matrix.rotate(Vector3f.YP.rotationDegrees(rotation))
        matrix.translate(0.0, (1.5f + lvt_7_1_ / 2.0f).toDouble(), 0.0)
        matrix.rotate(Quaternion(Vector3f(field_229047_f_, 0.0f, field_229047_f_), 60.0f, true))
        cube1.render(matrix, builder, packedLightIn, noOverlay)
        matrix.scale(0.875f, 0.875f, 0.875f)
        matrix.rotate(Quaternion(Vector3f(field_229047_f_, 0.0f, field_229047_f_), 60.0f, true))
        matrix.rotate(Vector3f.YP.rotationDegrees(rotation))
        cube1.render(matrix, builder, packedLightIn, noOverlay)
        matrix.scale(0.875f, 0.875f, 0.875f)
        matrix.rotate(Quaternion(Vector3f(field_229047_f_, 0.0f, field_229047_f_), 60.0f, true))
        matrix.rotate(Vector3f.YP.rotationDegrees(rotation))
        cube2.render(matrix, builder, packedLightIn, noOverlay)
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
            EnderDragonRenderer.func_229059_a_(-deltaX, -deltaY + lvt_7_1_, -deltaZ, partialTicks, entity.innerRotation, matrix, bufferIn, packedLightIn)
        }
        super.render(entity, entityYaw, partialTicks, matrix, bufferIn, packedLightIn)
    }

    override fun getEntityTexture(entity: EnderCrystalEntity): ResourceLocation = ENDER_CRYSTAL_TEXTURES

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
        private val field_229047_f_ = sin(0.7853981633974483).toFloat()

        fun func_229051_a_(entity: EnderCrystalEntity, ticks: Float): Float
        {
            val rotation = entity.innerRotation.toFloat() + ticks
            var i = MathHelper.sin(rotation * 0.2f) / 2.0f + 0.5f
            i = (i * i + i) * 0.4f
            return i - 1.4f
        }
    }

    init
    {
        shadowSize = 0.5f
        cube1 = ModelRenderer(64, 32, 0, 0)
        cube1.addBox(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f)
        cube2 = ModelRenderer(64, 32, 32, 0)
        cube2.addBox(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f)
        base = ModelRenderer(64, 32, 0, 16)
        base.addBox(-6.0f, 0.0f, -6.0f, 12.0f, 4.0f, 12.0f)
    }
}
