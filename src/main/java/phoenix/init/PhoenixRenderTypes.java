package phoenix.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.lwjgl.opengl.GL11;
import phoenix.utils.ResourseUtils;

import static net.minecraft.client.renderer.RenderState.*;

public class PhoenixRenderTypes
{

    public static RenderType TANK;
    public static final RenderState.TextureState TANK_STATE = new RenderState.TextureState(ResourseUtils.block("tank"), false, false);
    public static SimpleTexture TANK_TEXTURE = new SimpleTexture(ResourseUtils.block("tank"));
    public static void init()
    {
        Minecraft.getInstance().getTextureManager().loadTexture(ResourseUtils.block("tank"), TANK_TEXTURE);
        TANK  = createType("tank", DefaultVertexFormats.POSITION_COLOR_TEX,
                RenderType.State.getBuilder()
                        .shadeModel(SHADE_ENABLED)
                        .lightmap(LIGHTMAP_ENABLED)
                        .texture(TANK_STATE)
                        .alpha(HALF_ALPHA).build(true));

    }

    private static RenderType createType(String name, VertexFormat vertexFormat, RenderType.State state) {
        return createType(name, vertexFormat, GL11.GL_QUADS, 32768, state);
    }

    private static RenderType createType(String name, VertexFormat vertexFormat, int glDrawMode, int bufferSize, RenderType.State state) {
        return createType(name, vertexFormat, glDrawMode, bufferSize, false, false, state);
    }

    private static RenderType createType(String name, VertexFormat vertexFormat, int glDrawMode, int bufferSize, boolean usesDelegateDrawing, boolean sortVertices, RenderType.State state) {
        return RenderType.makeType(ResourseUtils.key(name).toString(), vertexFormat, glDrawMode, bufferSize, usesDelegateDrawing, sortVertices, state);
    }
}