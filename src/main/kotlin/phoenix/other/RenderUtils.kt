@file:Suppress("NOTHING_TO_INLINE")

package phoenix.other

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.AbstractGui
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.BufferBuilder
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldVertexBufferUploader
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import kotlin.math.sqrt

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
    textureManager?.bindTexture(texture)
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

inline fun FontRenderer.drawRightAlignedString(string: String, x: Int, y: Int, colour: Int)
{
    drawStringWithShadow(string, (x - this.getStringWidth(string)).toFloat(), y.toFloat(), colour)
}

fun getTextureSize(texture: ResourceLocation): Pair<Int, Int>
{
    val prevTexture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D)
    Minecraft.getInstance().getTextureManager().bindTexture(texture)
    val width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH)
    val height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT)
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, prevTexture)
    return Pair(width, height)
}

inline fun matrix(block : () -> Unit)
{
    RenderSystem.pushMatrix()
    block()
    RenderSystem.popMatrix()
}

inline fun scale(x : Float, y : Float, z : Float, block : () -> Unit)
{
    RenderSystem.scalef(x, y, z)
    block()
    RenderSystem.scalef(1f / x, 1f / y, 1f / z)
}

inline fun scale(x : Float, y : Float, block : () -> Unit)
{
    RenderSystem.scalef(x, y, 1f)
    block()
    RenderSystem.scalef(1f / x, 1f / y, 1f)
}

inline fun scale(scale : Float, block : () -> Unit)
{
    RenderSystem.scalef(scale, scale, 1f)
    block()
    RenderSystem.scalef(1f / scale, 1f / scale, 1f)
}

inline fun translate(x : Float, y : Float, z : Float, block : () -> Unit)
{
    RenderSystem.translatef(x, y, z)
    block()
    RenderSystem.translatef(-x, -y, -z)
}

inline fun translate(x : Float, y : Float, block : () -> Unit)
{
    RenderSystem.translatef(x, y, 1f)
    block()
    RenderSystem.translatef(-x, -y, 0f)
}