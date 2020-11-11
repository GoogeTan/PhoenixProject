package phoenix.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import phoenix.client.models.block.TankModel;
import phoenix.tile.redo.TankTile;

public class TankRenderer extends TileEntityRenderer<TankTile>
{
    public TankRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TankTile te, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        new TankModel(te).render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }
}