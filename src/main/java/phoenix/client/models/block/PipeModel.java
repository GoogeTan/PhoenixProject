package phoenix.client.models.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import phoenix.blocks.redo.PipeBlock;
import phoenix.init.PhoenixRenderTypes;

public class PipeModel extends Model
{
    ModelRenderer base;
    ModelRenderer n;
    ModelRenderer s;
    ModelRenderer w;
    ModelRenderer e;
    ModelRenderer u;
    ModelRenderer d;

    BlockState state;
    public PipeModel(BlockState stateIn)
    {
        super((param) -> PhoenixRenderTypes.PIPE);
        textureHeight = 64;
        textureWidth  = 64;
        state = stateIn;
        base = new ModelRenderer(this, 64, 64);
        n = new ModelRenderer(this, 0, 0);
        s = new ModelRenderer(this, 0, 0);
        w = new ModelRenderer(this, 0, 0);
        e = new ModelRenderer(this, 0, 0);
        u = new ModelRenderer(this, 0, 0);
        d = new ModelRenderer(this, 0, 0);

        base.setRotationPoint(4, 4, 4);

        n.setRotationPoint(4,         4, 4 - 4);
        s.setRotationPoint(4,         4, 4 + 8);
        e.setRotationPoint(4 + 8,     4, 4);
        w.setRotationPoint(4 - 4,     4, 4);
        u.setRotationPoint(4,     4 + 8, 4);
        d.setRotationPoint(4,     4 - 4, 4);

        base.addBox(0 ,0, 0, 8, 8, 8);

        n.addBox(0 ,0, 0, 8, 8, 4);
        s.addBox(0 ,0, 0, 8, 8, 4);
        e.addBox(0 ,0, 0, 4, 8, 8);
        w.addBox(0 ,0, 0, 4, 8, 8);
        u.addBox(0 ,0, 0, 8, 4, 8);
        d.addBox(0 ,0, 0, 8, 4, 8);

    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        base.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        if(state.get(PipeBlock.NORTH))  n.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        if(state.get(PipeBlock.SOUTH))  s.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        if(state.get(PipeBlock.WEST))   w.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        if(state.get(PipeBlock.EAST))   e.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        if(state.get(PipeBlock.UP))     u.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        if(state.get(PipeBlock.DOWN))   d.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
