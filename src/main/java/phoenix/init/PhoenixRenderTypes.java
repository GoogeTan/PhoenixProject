package phoenix.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import phoenix.utils.ResourseUtils;

import static net.minecraft.client.renderer.RenderState.*;

public class PhoenixRenderTypes
{
    public static RenderType TANK;
    public static RenderType PIPE;

    public static void init()
    {
        TANK = initTexture(ResourseUtils.block("tank"), "tank");
        PIPE = initTexture(ResourseUtils.block("pipe_"),  "pipe_");
    }

    private static RenderType initTexture(ResourceLocation path, String name)
    {
        Minecraft.getInstance().getTextureManager().loadTexture(path, new SimpleTexture(path));
        return  createType(name, RenderType.State.getBuilder()
                        .shadeModel(SHADE_ENABLED)
                        .lightmap(LIGHTMAP_ENABLED)
                        .texture(new TextureState(path, false, false))
                        .alpha(HALF_ALPHA).build(true));
    }

    private static RenderType createType(String name, RenderType.State state)
    {
        return RenderType.makeType(ResourseUtils.key(name).toString(), DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS, 32768, false, false, state);
    }
}