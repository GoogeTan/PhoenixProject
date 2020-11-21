package phoenix.client.models.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import phoenix.enity.TalpaEntity;

public class TalpaModel extends EntityModel<TalpaEntity>
{
    public ModelRenderer body;
    public ModelRenderer[] paws = new ModelRenderer[4];
    public TalpaModel()
    {
    }

    @Override
    public void setLivingAnimations(TalpaEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick)
    {
        if(entityIn.isChild())
        {
            textureHeight = 16;
            textureWidth  = 16;
            body = new ModelRenderer(this, 0, 0);
            body.addBox(0, 0, 0, 3, 3, 5);
            body.setRotationPoint(-1, 18, -1);

            paws[0] = new ModelRenderer(this, 0, 8);
            paws[1] = new ModelRenderer(this, 0, 8);
            paws[2] = new ModelRenderer(this, 0, 8);
            paws[3] = new ModelRenderer(this, 0, 8);

            paws[0].addBox(0, 0, 0, 1, 1, 3);
            paws[1].addBox(0, 0, 0, 1, 1, 3);
            paws[2].addBox(0, 0, 0, 1, 1, 3);
            paws[3].addBox(0, 0, 0, 1, 1, 3);
        }
        else
        {
            textureHeight = 32;
            textureWidth  = 32;
            body = new ModelRenderer(this, 0, 0);
            body.addBox(0,0,0, 6, 6, 10);
            body.setRotationPoint(-2, 18, -2);

            paws[0] = new ModelRenderer(this, 0, 16);
            paws[1] = new ModelRenderer(this, 0, 16);
            paws[2] = new ModelRenderer(this, 0, 16);
            paws[3] = new ModelRenderer(this, 0, 16);

            paws[0].addBox(0,0,0,1, 2, 6);
            paws[1].addBox(0,0,0,1, 2, 6);
            paws[2].addBox(0,0,0,2, 1, 6);
            paws[3].addBox(0,0,0,2, 1, 6);
        }

        paws[0].setRotationPoint(+1 + 3, -2 + 2 + 20,  +1 + MathHelper.cos(limbSwing * 0.6662F) * 4F * limbSwingAmount);
        paws[1].setRotationPoint(-5 + 2, -2 + 2 + 20,  +1 + MathHelper.cos(limbSwing * 0.6662F) * 4F * limbSwingAmount);
        paws[2].setRotationPoint(-2 + 2, +1 + 3 + 20,  -5 + MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 4F * limbSwingAmount);
        paws[3].setRotationPoint(-2 + 2, -5 + 2 + 20,  -5 + MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 4F * limbSwingAmount);
        if(entityIn.isChild())
        {
            for (ModelRenderer part : paws)
            {
                part.rotationPointX /= 2;
                part.rotationPointZ /= 2;
            }
            paws[0].rotationPointY -= 1;
            paws[1].rotationPointY -= 1;
            paws[2].rotationPointY -= 3;
        }
    }

    @Override
    public void setRotationAngles(   TalpaEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        paws[0].rotateAngleY = -25;
        paws[1].rotateAngleY =  25;
        paws[2].rotateAngleX = -25;
        paws[3].rotateAngleX =  25;
    }

    @Override
    public void render(   MatrixStack matrixStackIn,    IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        for (ModelRenderer paw:paws)
        {
            paw.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }
    }
}
