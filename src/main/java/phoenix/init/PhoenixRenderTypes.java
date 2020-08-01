package phoenix.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import phoenix.Phoenix;

public class PhoenixRenderTypes
{
    protected static final RenderState.LightmapState LIGHTMAP_ENABLED = new RenderState.LightmapState(true);
    protected static final RenderState.ShadeModelState SHADE_ENABLED = new RenderState.ShadeModelState(true);
    public static RenderType getPipe()
    {
        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(Phoenix.MOD_ID, "textures/blocks/chorus_plant.png"));

        return RenderType.makeType
                ("pipe",
                        DefaultVertexFormats.BLOCK,
                        7,
                        2097152,
                        true,
                        false,
                        RenderType.State.getBuilder()
                                .shadeModel(SHADE_ENABLED)
                                .lightmap(LIGHTMAP_ENABLED)
                                .texture(new RenderState.TextureState(sprite.getAtlasTexture().getTextureLocation(), false, true)).build(true));
    }
}
