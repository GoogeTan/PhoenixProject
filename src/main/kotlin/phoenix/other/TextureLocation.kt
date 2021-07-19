package phoenix.other

import net.minecraft.util.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import org.lwjgl.opengl.GL11
import phoenix.init.PhxRenderTypes

class TextureLocation : ResourceLocation
{
    var width : Int = 0
    var height : Int = 0
    var hasNotCalculatedSize = false

    constructor(modId: String, path: String) : super(modId, path)
    constructor(path : String) : super(path)

    init
    {
        try
        {
            if (mc == null || textureManager == null)
                hasNotCalculatedSize = true
            else
            {
                val prevTexture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D)
                textureManager?.bindTexture(this)
                width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH)
                height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT)
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, prevTexture)
            }
        }
        catch (e : Exception) { hasNotCalculatedSize = true }
    }

    fun bind()
    {
        if(hasNotCalculatedSize)
        {
            val prevTexture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D)
            textureManager?.bindTexture(this)
            width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH)
            height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT)
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, prevTexture)
        }
        textureManager?.bindTexture(this)
    }

    @OnlyIn(Dist.CLIENT)
    fun getRenderType() = PhxRenderTypes.initTexture(this, this.path.split("/").last())
}