package phoenix.utils

import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

class TextureLocation : ResourceLocation
{
    val width : Int
    val height : Int

    constructor(modId: String, path: String) : super(modId, path)
    constructor(path : String) : super(path)

    init
    {
        val prevTexture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D)
        Minecraft.getInstance().getTextureManager().bindTexture(this)
        width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH)
        height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, prevTexture)
    }

    fun bind()
    {
        mc.getTextureManager().bindTexture(this)
    }
}