@file:Suppress("INACCESSIBLE_TYPE")

package phoenix.init

import net.minecraft.client.renderer.RenderState
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import org.lwjgl.opengl.GL11
import phoenix.other.blockOf
import phoenix.other.keyOf

@OnlyIn(Dist.CLIENT)
object PhxRenderTypes
{
    lateinit var tankTexture : RenderType
    lateinit var pipeTexture : RenderType
    val          eyesTexture : RenderType = RenderType.getEyes(ResourceLocation("phoenix", "textures/entity/cauda/cauda_eyes.png"))
    val   eyesTextureSpecial : RenderType = RenderType.getEyes(ResourceLocation("phoenix", "textures/entity/cauda/cauda_eyes_special.png"))

    fun init()
    {
        tankTexture = initTexture(blockOf("tank"), "tank")
        pipeTexture = initTexture(blockOf("pipe_"), "pipe_")
    }

    fun initTexture(path: ResourceLocation, name: String): RenderType
    {
        return createType(
            name,
            RenderType.State.getBuilder()
                .shadeModel(RenderState.SHADE_ENABLED)
                .lightmap(RenderState.LIGHTMAP_ENABLED)
                .texture(RenderState.TextureState(path, false, false))
                .alpha(RenderState.HALF_ALPHA).build(true)
        )
    }

    @OnlyIn(Dist.CLIENT)
    private fun createType(name: String, state: RenderType.State) = RenderType.makeType(keyOf(name).toString(), DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS, 32768, false, false, state)
}