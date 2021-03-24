package phoenix.utils

import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

object TextureUtils
{
    fun getTextureSize(texture: ResourceLocation): Pair<Int, Int>
    {
        val prevTexture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D)
        Minecraft.getInstance().getTextureManager().bindTexture(texture)
        val width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH)
        val height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, prevTexture)
        return Pair(width, height)
    }
}