package phoenix.client.models.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public class CaudaModel<T extends LivingEntity> extends EntityModel<T> {
    private final ModelRenderer body;
    private final ModelRenderer body2;
    private final ModelRenderer body3;
    private final ModelRenderer tail1;
    private final ModelRenderer tail2;
    private final ModelRenderer tail3;
    private final ModelRenderer clawLeft;
    private final ModelRenderer clawRight;
    private final ModelRenderer wingLeft;
    private final ModelRenderer wingLeft2;
    private final ModelRenderer wingLeft3;
    private final ModelRenderer wingLeft4;
    private final ModelRenderer wingRight;
    private final ModelRenderer wingRight2;
    private final ModelRenderer wingRight3;
    private final ModelRenderer wingRight4;

    public CaudaModel()
    {
        textureWidth = 128;
        textureHeight = 128;

        body = new ModelRenderer(this);
        body.setRotationPoint(1.0F, 18.0F, 0.0F);
        body.setTextureOffset(36, 0).addBox(-2.0F, -1.0F, -4.0F, 4.0F, 3.0F, 8.0F, 0.0F, false);

        body2 = new ModelRenderer(this);
        body2.setRotationPoint(-2.0F, 0.0F, 0.0F);
        body.addChild(body2);
        body2.setTextureOffset(60, 9).addBox(-3.0F, 0.0F, -4.0F, 3.0F, 1.0F, 8.0F, 0.0F, false);
        body2.setTextureOffset(60, 0).addBox(-3.0F, -1.0F, -3.0F, 3.0F, 3.0F, 6.0F, 0.0F, false);

        body3 = new ModelRenderer(this);
        body3.setRotationPoint(-3.0F, 0.0F, 0.0F);
        body2.addChild(body3);
        body3.setTextureOffset(12, 0).addBox(-4.0F, 0.0F, -4.0F, 4.0F, 1.0F, 8.0F, 0.0F, false);
        body3.setTextureOffset(0, 0).addBox(-4.0F, -1.0F, -2.0F, 4.0F, 3.0F, 4.0F, 0.0F, false);

        tail1 = new ModelRenderer(this);
        tail1.setRotationPoint(-6.0F, 0.0F, 0.0F);
        body3.addChild(tail1);
        tail1.setTextureOffset(30, 20).addBox(-6.0F, 0.0F, 1.0F, 8.0F, 1.0F, 3.0F, 0.0F, false);
        tail1.setTextureOffset(30, 16).addBox(-6.0F, 0.0F, -4.0F, 8.0F, 1.0F, 3.0F, 0.0F, true);

        tail2 = new ModelRenderer(this);
        tail2.setRotationPoint(-8.0F, 1.0F, 0.0F);
        tail1.addChild(tail2);
        tail2.setTextureOffset(30, 29).addBox(-4.0F, -1.0F, -3.0F, 6.0F, 1.0F, 2.0F, 0.0F, false);
        tail2.setTextureOffset(30, 26).addBox(-4.0F, -1.0F, 1.0F, 6.0F, 1.0F, 2.0F, 0.0F, false);

        tail3 = new ModelRenderer(this);
        tail3.setRotationPoint(-3.0F, 0.0F, 0.0F);
        tail2.addChild(tail3);
        tail3.setTextureOffset(30, 24).addBox(-8.0F, -1.0F, -2.0F, 7.0F, 1.0F, 1.0F, 0.0F, false);
        tail3.setTextureOffset(30, 24).addBox(-8.0F, -1.0F, 1.0F, 7.0F, 1.0F, 1.0F, 0.0F, false);

        clawLeft = new ModelRenderer(this);
        clawLeft.setRotationPoint(-4.0F, -1.0F, 2.0F);
        tail2.addChild(clawLeft);
        setRotationAngle(clawLeft, 0.0F, 0.2967F, 0.0F);
        clawLeft.setTextureOffset(30, 24).addBox(-7.0F, 0.0F, 0.0F, 7.0F, 1.0F, 1.0F, 0.0F, false);

        clawRight = new ModelRenderer(this);
        clawRight.setRotationPoint(-4.0F, -1.0F, -2.0F);
        tail2.addChild(clawRight);
        setRotationAngle(clawRight, 0.0F, -0.2967F, 0.0F);
        clawRight.setTextureOffset(30, 24).addBox(-7.0F, 0.0F, -1.0F, 7.0F, 1.0F, 1.0F, 0.0F, false);

        wingLeft = new ModelRenderer(this);
        wingLeft.setRotationPoint(3.0F, 16.0F, 4.0F);
        wingLeft.setTextureOffset(0, 27).addBox(-7.0F, 0.0F, 0.0F, 8.0F, 3.0F, 7.0F, 0.0F, false);
        wingLeft.setTextureOffset(52, 18).addBox(-10.0F, 1.0F, 0.0F, 3.0F, 1.0F, 7.0F, 0.0F, false);

        wingLeft2 = new ModelRenderer(this);
        wingLeft2.setRotationPoint(0.0F, 1.0F, 7.0F);
        wingLeft.addChild(wingLeft2);
        setRotationAngle(wingLeft2, 0.0F, 0.0F, 0.0F);
        wingLeft2.setTextureOffset(0, 37).addBox(-7.0F, 0.0F, 0.0F, 7.0F, 2.0F, 7.0F, 0.0F, false);

        wingLeft3 = new ModelRenderer(this);
        wingLeft3.setRotationPoint(0.0F, 1.0F, 7.0F);
        wingLeft2.addChild(wingLeft3);
        setRotationAngle(wingLeft3, 0.0F, 0.0F, 0.0F);
        wingLeft3.setTextureOffset(28, 32).addBox(-6.0F, 0.0F, 0.0F, 6.0F, 1.0F, 6.0F, 0.0F, false);

        wingLeft4 = new ModelRenderer(this);
        wingLeft4.setRotationPoint(0.0F, 0.0F, 6.0F);
        wingLeft3.addChild(wingLeft4);
        wingLeft4.setTextureOffset(16, 9).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);

        wingRight = new ModelRenderer(this);
        wingRight.setRotationPoint(3.0F, 16.0F, -4.0F);
        wingRight.setTextureOffset(0, 17).addBox(-7.0F, 0.0F, -7.0F, 8.0F, 3.0F, 7.0F, 0.0F, false);
        wingRight.setTextureOffset(52, 26).addBox(-10.0F, 1.0F, -7.0F, 3.0F, 1.0F, 7.0F, 0.0F, false);

        wingRight2 = new ModelRenderer(this);
        wingRight2.setRotationPoint(0.0F, 1.0F, -7.0F);
        wingRight.addChild(wingRight2);
        wingRight2.setTextureOffset(0, 46).addBox(-7.0F, 0.0F, -7.0F, 7.0F, 2.0F, 7.0F, 0.0F, false);

        wingRight3 = new ModelRenderer(this);
        wingRight3.setRotationPoint(0.0F, 1.0F, -7.0F);
        wingRight2.addChild(wingRight3);
        wingRight3.setTextureOffset(28, 39).addBox(-6.0F, 0.0F, -6.0F, 6.0F, 1.0F, 6.0F, 0.0F, false);

        wingRight4 = new ModelRenderer(this);
        wingRight4.setRotationPoint(0.0F, 0.0F, -6.0F);
        wingRight3.addChild(wingRight4);
        wingRight4.setTextureOffset(0, 9).addBox(-2.0F, 0.0F, -6.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        float ticks = ((float)(entity.getEntityId() * 3) + ageInTicks) * 0.13F;
        if(Math.cos(ticks / 20) - 0.3 > 0)
        {
            wingLeft  .rotateAngleX = MathHelper.cos(ticks) * 16.0F * ((float) Math.PI / 180F);
            wingLeft2 .rotateAngleX = MathHelper.cos(ticks) * 16.0F * ((float) Math.PI / 180F);
            wingRight .rotateAngleX = -wingLeft.rotateAngleX;
            wingRight2.rotateAngleX = -wingLeft.rotateAngleX;
        }
        else
        {
            wingLeft  .rotateAngleX = 0;
            wingLeft2 .rotateAngleX = 0;
            wingRight .rotateAngleX = 0;
            wingRight2.rotateAngleX = 0;
        }

        tail3    .rotateAngleZ =  (5.0F + MathHelper.cos(ticks * 2.0F) * 7.0F) * ((float)Math.PI / 180F);
        clawLeft .rotateAngleZ = -(5.0F + MathHelper.cos(ticks * 2.0F) * 7.0F) * ((float)Math.PI / 180F);
        clawRight.rotateAngleZ = -(5.0F + MathHelper.cos(ticks * 2.0F) * 7.0F) * ((float)Math.PI / 180F);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        body.render(matrixStack, buffer, packedLight, packedOverlay);
        wingLeft.render(matrixStack, buffer, packedLight, packedOverlay);
        wingRight.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}