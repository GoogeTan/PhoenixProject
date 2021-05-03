package phoenix.utils

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.AbstractGui
import net.minecraft.client.renderer.BufferBuilder
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldVertexBufferUploader
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import kotlin.math.sqrt


object RenderUtils
{
    fun drawRectScalable(texture: TextureLocation, x: Int, y: Int, maxSizeX: Double, maxSizeY: Double, depth: Int)
    {
        texture.bind()
        val width = texture.width
        val height = texture.height
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f)
        var vec = Vec2(width.toDouble(), height.toDouble())
        vec.toUnit()
        vec *= sqrt(maxSizeX * maxSizeX + maxSizeY * maxSizeY)
        val sizeX = vec.x.toInt()
        val sizeY = vec.y.toInt()
        blit(x, y, depth, 0f, 0f, sizeX, sizeY, sizeX, sizeY)
    }

    fun drawTexturedRectWithSize(
        texture: ResourceLocation,
        textureSizeX: Int,
        textureSizeY: Int,
        x: Int,
        y: Int,
        sizeX: Int,
        sizeY: Int,
        blitOffset: Int
    )
    {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f)
        mc.getTextureManager().bindTexture(texture)
        blit(x, y, blitOffset, 0f, 0f, sizeX, sizeY, textureSizeX, textureSizeY)
    }

    fun drawTexturedRectWithSize(
        texture: ResourceLocation,
        textureSizeX: Int,
        textureSizeY: Int,
        x: Int,
        y: Int,
        sizeX: Int,
        sizeY: Int,
        blitOffset: Int,
        u: Int,
        v: Int
    )
    {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f)
        Minecraft.getInstance().getTextureManager().bindTexture(texture)
        AbstractGui.blit(x, y, blitOffset, u.toFloat(), v.toFloat(), sizeX, sizeY, textureSizeX, textureSizeY)
    }

    fun blit(x: Int, y: Int, offsetU: Float, offsetV: Float, sizeX: Int, sizeY: Int) = blit(x, y, 0, offsetU, offsetV, sizeX, sizeY, 256, 256)

    fun blit(
        x: Int,
        y: Int,
        depth: Int,
        offsetU: Float,
        offsetV: Float,
        sizeX: Int,
        sizeY: Int,
        texSizeX: Int,
        texSizeY: Int
    )
    {
        draw(x, x + sizeX, y, y + sizeY, depth, offsetU, offsetV, texSizeX.toFloat(), texSizeY.toFloat(), sizeX, sizeY)
    }

    private fun draw(
        posA: Int,
        posB: Int,
        posC: Int,
        posD: Int,
        depth: Int,
        offsetU: Float,
        offsetV: Float,
        texSizeX: Float,
        texSizeY: Float,
        sizeX: Int,
        sizeY: Int
    )
    {
        //*
        innerBlit(
            posA, posB, posC, posD, depth,
            (offsetU + 0.00f) / texSizeX,
            (offsetU + sizeX) / texSizeX,
            (offsetV + 0.00f) / texSizeY,
            (offsetV + sizeY) / texSizeY
        )

        //*/
        /*
        innerBlit(posA, posB, posC, posD, depth,
                (offsetU + 0.00F) / texSizeX,
                (offsetU + texSizeX) / texSizeX,
                (offsetV + 0.00F) / texSizeY,
                (offsetV + texSizeY) / texSizeY);
        // */
    }

    fun innerBlit(
        posA: Int,
        posB: Int,
        posC: Int,
        posD: Int,
        depth: Int,
        uvA: Float,
        uvB: Float,
        uvC: Float,
        uvD: Float
    )
    {
        val bufferbuilder = Tessellator.getInstance().buffer
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX)
        bufferbuilder.pos(posA.toDouble(), posD.toDouble(), depth.toDouble()).tex(uvA, uvD).endVertex()
        bufferbuilder.pos(posB.toDouble(), posD.toDouble(), depth.toDouble()).tex(uvB, uvD).endVertex()
        bufferbuilder.pos(posB.toDouble(), posC.toDouble(), depth.toDouble()).tex(uvB, uvC).endVertex()
        bufferbuilder.pos(posA.toDouble(), posC.toDouble(), depth.toDouble()).tex(uvA, uvC).endVertex()
        bufferbuilder.finishDrawing()
        RenderSystem.enableAlphaTest()
        WorldVertexBufferUploader.draw(bufferbuilder)
    }

    fun refreshDrawing(vb: IVertexBuilder, type: RenderType)
    {
        if (vb is BufferBuilder)
        {
            type.finish(vb, 0, 0, 0)
            vb.begin(type.getDrawMode(), type.getVertexFormat())
        }
    }
}
