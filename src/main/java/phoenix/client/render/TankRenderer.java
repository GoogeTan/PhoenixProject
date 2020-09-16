package phoenix.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import phoenix.client.models.TankModel;
import phoenix.tile.TankTile;
import phoenix.world.EndBiomedDimension;

public class TankRenderer extends TileEntityRenderer<TankTile>
{
    ResourceLocation TEXTURE_FLUID;

    public Material MATERIAL_FLUID;

    public TankRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TankTile te, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        try
        {
            TEXTURE_FLUID = te.getInput().getFluid().getFluid().getAttributes().getStillTexture();
        } catch (Exception e)
        {
            TEXTURE_FLUID = null;
        }
        if (TEXTURE_FLUID != null)
        {
            MATERIAL_FLUID = new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE, TEXTURE_FLUID);
            TankModel model = new TankModel(te);
            matrixStackIn.push();
            IVertexBuilder fluid_builder = MATERIAL_FLUID.getBuffer(bufferIn, RenderType::getEntitySolid);
            if (te.tank.getFluid().getFluid() != Fluids.WATER)
            {
                model.render(matrixStackIn, fluid_builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            } else
            {
                if (te.getWorld().dimension instanceof EndBiomedDimension)
                {
                    model.render(matrixStackIn, fluid_builder, combinedLightIn, combinedOverlayIn, 80 / 256F, 43 / 256F, 226 / 256F, 0.7F);
                }
                else
                {
                    int watercolor = te.getWorld().getBiome(te.getPos()).getWaterColor();
                    model.render(matrixStackIn, fluid_builder, combinedLightIn, combinedOverlayIn, (watercolor / 10000) / 100f, ((watercolor / 100) % 100) / 100f, 1.0F, 0.7F);
                }
            }
            matrixStackIn.pop();
        }
    }
}
