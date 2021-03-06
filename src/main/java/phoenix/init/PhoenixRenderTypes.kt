package phoenix.init

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderState
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.texture.SimpleTexture
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import org.lwjgl.opengl.GL11
import phoenix.utils.ResourseUtils.block
import phoenix.utils.ResourseUtils.key

@OnlyIn(Dist.CLIENT)
object PhoenixRenderTypes
{
    lateinit var TANK: RenderType
    lateinit var PIPE: RenderType

    fun init()
    {
        TANK = initTexture(block("tank"), "tank")
        PIPE = initTexture(block("pipe_"), "pipe_")
    }

    private fun initTexture(path: ResourceLocation, name: String): RenderType
    {
        Minecraft.getInstance().getTextureManager().loadTexture(path, SimpleTexture(path))
        return createType(name,
            RenderType.State.getBuilder()
                .shadeModel(RenderState.SHADE_ENABLED)
                .lightmap(RenderState.LIGHTMAP_ENABLED)
                .texture(RenderState.TextureState(path, false, false))
                .alpha(RenderState.HALF_ALPHA).build(true))
    }

    private fun createType(name: String, state: RenderType.State) = RenderType.makeType(key(name).toString(), DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS, 32768, false, false, state)
}