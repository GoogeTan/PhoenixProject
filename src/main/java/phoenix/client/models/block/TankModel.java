package phoenix.client.models.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import phoenix.tile.redo.TankTile;

public class TankModel extends Model
{
    ResourceLocation TEXTURE_FLUID;
    public Material MATERIAL_FLUID;
    public static final Material TEXTURE = new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE,
            new ResourceLocation("block/tank"));
    public TankTile tileTank;
    public ModelRenderer fluid = new ModelRenderer(this, 0,0);
    public ModelRenderer block = new ModelRenderer(this, 0, 0);

    public TankModel(TankTile tankIn)
    {
        super(RenderType::getEntitySolid);

        tileTank = tankIn;
        fluid.setRotationPoint(0, 0, 0);
        fluid.addBox(2, 2, 2, 12, 12 * tileTank.getInput().getFluidAmount() / tileTank.getInput().getCapacity(), 12);
        block.setRotationPoint(0, 0, 0);
        block.addBox(0, 0, 0, 16,16, 16);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        block.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        fluid.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer buffer, int packedLightIn, int packedOverlayIn)
    {
        matrixStackIn.push();
        try
        {
            TEXTURE_FLUID = tileTank.getInput().getFluid().getFluid().getAttributes().getStillTexture();
        } catch (Exception e)
        {
            TEXTURE_FLUID = null;
        }
        if (TEXTURE_FLUID != null)
        {
            MATERIAL_FLUID = new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE, TEXTURE_FLUID);

            IVertexBuilder fluid_builder = MATERIAL_FLUID.getBuffer(buffer, RenderType::getEntitySolid);
            if (tileTank.getTank().getFluid().getFluid() != Fluids.WATER)
            {
                fluid.render(matrixStackIn, fluid_builder, packedLightIn, packedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            } else
            {
                if (tileTank.getWorld().dimension.getType() == DimensionType.THE_END)
                {
                    fluid.render(matrixStackIn, fluid_builder, packedLightIn, packedOverlayIn, 80 / 256F, 43 / 256F, 226 / 256F, 0.7F);
                }
                else
                {
                    int watercolor = tileTank.getWorld().getBiome(tileTank.getPos()).getWaterColor();
                    fluid.render(matrixStackIn, fluid_builder, packedLightIn, packedOverlayIn, (watercolor / 10000) / 100f, ((watercolor / 100) % 100) / 100f, 1.0F, 0.7F);
                }
            }

        }
        //block.render(matrixStackIn, TEXTURE.getBuffer(buffer, RenderType::getEntityCutout), packedLightIn, packedOverlayIn);
        matrixStackIn.pop();
    }
}

