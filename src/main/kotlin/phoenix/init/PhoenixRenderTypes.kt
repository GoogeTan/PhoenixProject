package phoenix.init

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
import phoenix.utils.mc

@OnlyIn(Dist.CLIENT)
object PhoenixRenderTypes
{
    lateinit var tankTexture : RenderType
    lateinit var pipeTexture : RenderType
    val          eyesTexture : RenderType = RenderType.getEyes(ResourceLocation("phoenix", "textures/entity/cauda/cauda_eyes.png"))

    fun init()
    {
        tankTexture = initTexture(block("tank"), "tank")
        pipeTexture = initTexture(block("pipe_"), "pipe_")
    }

    fun initTexture(path: ResourceLocation, name: String): RenderType
    {
        mc.getTextureManager().loadTexture(path, SimpleTexture(path))
        return createType(name,
            RenderType.State.getBuilder()
                .shadeModel(RenderState.SHADE_ENABLED)
                .lightmap(RenderState.LIGHTMAP_ENABLED)
                .texture(RenderState.TextureState(path, false, false))
                .alpha(RenderState.HALF_ALPHA).build(true))
    }

    private fun createType(name: String, state: RenderType.State) = RenderType.makeType(key(name).toString(), DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS, 32768, false, false, state)
}