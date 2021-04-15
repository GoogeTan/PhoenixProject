package phoenix.client.render.entity

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.util.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import phoenix.enity.boss.balls.ExplosiveBallEntity

@OnlyIn(Dist.CLIENT)
class ExplosiveBallRenderer(renderManagerIn: EntityRendererManager) : EntityRenderer<ExplosiveBallEntity>(renderManagerIn)
{
    override fun getBlockLight(entityIn: ExplosiveBallEntity, partialTicks: Float): Int = 15

    override fun render(
        entityIn: ExplosiveBallEntity,
        entityYaw: Float,
        partialTicks: Float,
        matrixStackIn: MatrixStack,
        bufferIn: IRenderTypeBuffer,
        packedLightIn: Int
    )
    {
        matrixStackIn.push()
        matrixStackIn.scale(2.0f, 2.0f, 2.0f)
        matrixStackIn.rotate(renderManager.cameraOrientation)
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0f))
        val lastMatrics = matrixStackIn.last
        val matrix4f = lastMatrics.matrix
        val matrix3f = lastMatrics.normal
        val ivertexbuilder = bufferIn.getBuffer(TEXTURE_TYPE)
        func_229045_a_(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 0.0f, 0, 0, 1)
        func_229045_a_(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 1.0f, 0, 1, 1)
        func_229045_a_(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 1.0f, 1, 1, 0)
        func_229045_a_(ivertexbuilder, matrix4f, matrix3f, packedLightIn, 0.0f, 1, 0, 0)
        matrixStackIn.pop()
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn)
    }

    override fun getEntityTexture(entity: ExplosiveBallEntity): ResourceLocation = DRAGON_FIREBALL_TEXTURE

    companion object
    {
        private val DRAGON_FIREBALL_TEXTURE = ResourceLocation("textures/entity/enderdragon/dragon_fireball.png")
        private val TEXTURE_TYPE = RenderType.getEntityCutoutNoCull(
            DRAGON_FIREBALL_TEXTURE
        )

        private fun func_229045_a_(
            buffer: IVertexBuilder,
            mat1: Matrix4f,
            mat2: Matrix3f,
            light: Int,
            ticks: Float,
            smth: Int,
            u: Int,
            v: Int
        )
        {
            buffer.pos(mat1, ticks - 0.5f, smth.toFloat() - 0.25f, 0.0f)
                .color(255, 255, 255, 255).tex(
                    u.toFloat(), v.toFloat()
                ).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(mat2, 0.0f, 1.0f, 0.0f).endVertex()
        }
    }
}
