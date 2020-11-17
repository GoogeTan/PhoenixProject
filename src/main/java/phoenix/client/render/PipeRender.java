package phoenix.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import phoenix.client.models.block.PipeModel;
import phoenix.init.PhoenixRenderTypes;
import phoenix.tile.redo.PipeTile;
import phoenix.utils.RenderUtils;

public class PipeRender extends TileEntityRenderer<PipeTile>
{
    public static final Material TEXTURE = new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE,
                                    new ResourceLocation("block/chorus_plant"));
    public PipeRender(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(PipeTile tile, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int light, int overlay)
    {
        PipeModel model = new PipeModel(tile.getBlockState());
        matrix.push();
        IVertexBuilder builder = buffer.getBuffer(PhoenixRenderTypes.PIPE);
        model.render(matrix, builder, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        RenderUtils.refreshDrawing(builder, PhoenixRenderTypes.PIPE);
        matrix.pop();
    }
}
