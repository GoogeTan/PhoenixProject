package phoenix.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
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
        PIPE = initTexture(ResourseUtils.block("pipe"),  "pipe");
    }

    private static RenderType initTexture(ResourceLocation path, String name)
    {
        Minecraft.getInstance().getTextureManager().loadTexture(path, new SimpleTexture(path));
        return  createType(name, DefaultVertexFormats.POSITION_COLOR_TEX,
                RenderType.State.getBuilder()
                        .shadeModel(SHADE_ENABLED)
                        .lightmap(LIGHTMAP_ENABLED)
                        .texture(new TextureState(path, false, false))
                        .alpha(HALF_ALPHA).build(true));
    }

    private static RenderType createType(String name, VertexFormat vertexFormat, RenderType.State state)
    {
        return createType(name, vertexFormat, GL11.GL_QUADS, 32768, false, false, state);
    }

    private static RenderType createType(String name, VertexFormat vertexFormat, int glDrawMode, int bufferSize, boolean usesDelegateDrawing, boolean sortVertices, RenderType.State state)
    {
        return RenderType.makeType(ResourseUtils.key(name).toString(), vertexFormat, glDrawMode, bufferSize, usesDelegateDrawing, sortVertices, state);
    }
}