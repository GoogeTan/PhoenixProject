// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


public class custom_model extends EntityModel<Entity> {
	private final ModelRenderer body;
	private final ModelRenderer body2;
	private final ModelRenderer body3;
	private final ModelRenderer tail1;
	private final ModelRenderer tail2;
	private final ModelRenderer tail3;
	private final ModelRenderer wingLeft;
	private final ModelRenderer wingLeft2;
	private final ModelRenderer wingLeft3;
	private final ModelRenderer wingRight;
	private final ModelRenderer wingRight2;
	private final ModelRenderer wingRight3;

	public custom_model() {
		textureWidth = 128;
		textureHeight = 128;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 24.0F, 0.0F);
		body.setTextureOffset(0, 0).addBox(-1.0F, -7.0F, -4.0F, 4.0F, 3.0F, 8.0F, 0.0F, false);

		body2 = new ModelRenderer(this);
		body2.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addChild(body2);
		body2.setTextureOffset(0, 0).addBox(-4.0F, -6.0F, -4.0F, 3.0F, 1.0F, 8.0F, 0.0F, false);
		body2.setTextureOffset(0, 0).addBox(-4.0F, -7.0F, -3.0F, 3.0F, 3.0F, 6.0F, 0.0F, false);

		body3 = new ModelRenderer(this);
		body3.setRotationPoint(0.0F, 0.0F, 0.0F);
		body2.addChild(body3);
		body3.setTextureOffset(0, 0).addBox(-7.0F, -6.0F, -4.0F, 3.0F, 1.0F, 8.0F, 0.0F, false);
		body3.setTextureOffset(0, 0).addBox(-6.0F, -7.0F, -2.0F, 2.0F, 3.0F, 4.0F, 0.0F, false);

		tail1 = new ModelRenderer(this);
		tail1.setRotationPoint(0.0F, 0.0F, 0.0F);
		body3.addChild(tail1);
		tail1.setTextureOffset(0, 0).addBox(-11.0F, -6.0F, 1.0F, 4.0F, 1.0F, 3.0F, 0.0F, false);
		tail1.setTextureOffset(0, 0).addBox(-11.0F, -6.0F, -4.0F, 4.0F, 1.0F, 3.0F, 0.0F, false);

		tail2 = new ModelRenderer(this);
		tail2.setRotationPoint(-7.0F, -5.0F, -1.0F);
		tail1.addChild(tail2);
		tail2.setTextureOffset(0, 0).addBox(-8.0F, -1.0F, -2.0F, 4.0F, 1.0F, 2.0F, 0.0F, false);
		tail2.setTextureOffset(0, 0).addBox(-8.0F, -1.0F, 2.0F, 4.0F, 1.0F, 2.0F, 0.0F, false);

		tail3 = new ModelRenderer(this);
		tail3.setRotationPoint(0.0F, 0.0F, 0.0F);
		tail2.addChild(tail3);
		tail3.setTextureOffset(0, 0).addBox(-15.0F, -1.0F, -1.0F, 7.0F, 1.0F, 1.0F, 0.0F, false);
		tail3.setTextureOffset(0, 0).addBox(-15.0F, -1.0F, 2.0F, 7.0F, 1.0F, 1.0F, 0.0F, false);

		wingLeft = new ModelRenderer(this);
		wingLeft.setRotationPoint(0.0F, 24.0F, 0.0F);
		wingLeft.setTextureOffset(0, 0).addBox(-5.0F, -8.0F, 4.0F, 8.0F, 3.0F, 7.0F, 0.0F, false);

		wingLeft2 = new ModelRenderer(this);
		wingLeft2.setRotationPoint(0.0F, -5.0F, 4.0F);
		wingLeft.addChild(wingLeft2);
		wingLeft2.setTextureOffset(0, 0).addBox(-4.0F, -2.0F, 7.0F, 7.0F, 2.0F, 7.0F, 0.0F, false);

		wingLeft3 = new ModelRenderer(this);
		wingLeft3.setRotationPoint(0.0F, 0.0F, 0.0F);
		wingLeft2.addChild(wingLeft3);
		wingLeft3.setTextureOffset(0, 0).addBox(-3.0F, -1.0F, 14.0F, 6.0F, 1.0F, 6.0F, 0.0F, false);

		wingRight = new ModelRenderer(this);
		wingRight.setRotationPoint(0.0F, 24.0F, 0.0F);
		wingRight.setTextureOffset(0, 0).addBox(-5.0F, -8.0F, -11.0F, 8.0F, 3.0F, 7.0F, 0.0F, false);

		wingRight2 = new ModelRenderer(this);
		wingRight2.setRotationPoint(0.0F, 0.0F, 0.0F);
		wingRight.addChild(wingRight2);
		wingRight2.setTextureOffset(0, 0).addBox(-4.0F, -7.0F, -18.0F, 7.0F, 2.0F, 7.0F, 0.0F, false);

		wingRight3 = new ModelRenderer(this);
		wingRight3.setRotationPoint(0.0F, 0.0F, 0.0F);
		wingRight2.addChild(wingRight3);
		wingRight3.setTextureOffset(0, 0).addBox(-3.0F, -6.0F, -24.0F, 6.0F, 1.0F, 6.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
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