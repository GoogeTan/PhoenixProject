package phoenix.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import phoenix.Phoenix;
import phoenix.client.models.PipeModel;
import phoenix.tile.PipeTile;

public class PipeRender extends TileEntityRenderer<PipeTile>
{
    public static final Material TEXTURE_PIPE = new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE, new ResourceLocation(Phoenix.MOD_ID, "textures/blocks/chorus_plant.png"));
    public PipeRender(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(PipeTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        PipeModel model = new PipeModel(tileEntityIn.getBlockState());
        matrixStackIn.push();
        IVertexBuilder ivertexbuilder = TEXTURE_PIPE.getBuffer(bufferIn, model::getRenderType);
        model.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
    }
}
