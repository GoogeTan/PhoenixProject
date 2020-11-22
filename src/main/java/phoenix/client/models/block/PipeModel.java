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

        base.setRotationPoint(6, 6, 6);

        n.setRotationPoint(6,     6, 0);
        s.setRotationPoint(6,     6, 6 + 4);
        e.setRotationPoint(6 + 4, 6, 6);
        w.setRotationPoint(0,     6, 6);
        u.setRotationPoint(6,     6 + 4, 6);
        d.setRotationPoint(6,     0, 6);

        base.addBox(0 ,0, 0, 4, 4, 4);

        n.addBox(0 ,0, 0, 4, 4, 6);
        s.addBox(0 ,0, 0, 4, 4, 6);
        e.addBox(0 ,0, 0, 6, 4, 4);
        w.addBox(0 ,0, 0, 6, 4, 4);
        u.addBox(0 ,0, 0, 4, 6, 4);
        d.addBox(0 ,0, 0, 4, 6, 4);
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
