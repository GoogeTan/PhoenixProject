package phoenix.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import phoenix.tile.redo.TankTile;

public class TankModel extends Model
{
    public TankTile tileTank;
    public ModelRenderer fluid = new ModelRenderer(this, 0,0);

    public TankModel(TankTile tankIn)
    {
        super(RenderType::getEntitySolid);
        tileTank = tankIn;
        fluid.setRotationPoint(0, 0, 0);
        fluid.addBox(2, 2, 2, 12, 12 * tileTank.getInput().getFluidAmount() / tileTank.getInput().getCapacity(), 12);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        fluid.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}

