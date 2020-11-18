package phoenix.client.models.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class Cauda2Model<T extends Entity> extends EntityModel<T>
{
	private final ModelRenderer body;
	private final ModelRenderer legR1;
	private final ModelRenderer legR2;
	private final ModelRenderer legL1;
	private final ModelRenderer legL2;
	private final ModelRenderer head;
	private final ModelRenderer cube_r1;
	private final ModelRenderer body2;
	private final ModelRenderer tailL;
	private final ModelRenderer tailL2;
	private final ModelRenderer clawLL;
	private final ModelRenderer clawLL2;
	private final ModelRenderer clawLL3;
	private final ModelRenderer clawLM;
	private final ModelRenderer clawLM2;
	private final ModelRenderer clawLM3;
	private final ModelRenderer clawLR;
	private final ModelRenderer clawLR1;
	private final ModelRenderer clawLR2;
	private final ModelRenderer tailM;
	private final ModelRenderer tailM2;
	private final ModelRenderer clawML;
	private final ModelRenderer clawML1;
	private final ModelRenderer clawML2;
	private final ModelRenderer clawMM;
	private final ModelRenderer clawML4;
	private final ModelRenderer clawML5;
	private final ModelRenderer clawMR;
	private final ModelRenderer clawMR2;
	private final ModelRenderer clawMR3;
	private final ModelRenderer tailR;
	private final ModelRenderer tailL4;
	private final ModelRenderer clawLL4;
	private final ModelRenderer clawLL5;
	private final ModelRenderer clawLL6;
	private final ModelRenderer clawLM4;
	private final ModelRenderer clawLM5;
	private final ModelRenderer clawLM6;
	private final ModelRenderer clawLR3;
	private final ModelRenderer clawLR4;
	private final ModelRenderer clawLR5;
	private final ModelRenderer wingL;
	private final ModelRenderer wilgL2;
	private final ModelRenderer wildL3;
	private final ModelRenderer wingL4;
	private final ModelRenderer wingL5;
	private final ModelRenderer wingR;
	private final ModelRenderer wingR2;
	private final ModelRenderer wingR3;
	private final ModelRenderer wingR4;
	private final ModelRenderer wingR5;

	public Cauda2Model() {
		textureWidth = 128;
		textureHeight = 128;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 19.0F, -5.0F);
		body.setTextureOffset(0, 0).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 3.0F, 9.0F, 0.0F, false);
		body.setTextureOffset(0, 0).addBox(1.0F, -1.0F, 6.0F, 2.0F, 1.0F, 3.0F, 0.0F, false);
		body.setTextureOffset(0, 0).addBox(-3.0F, -1.0F, 6.0F, 2.0F, 1.0F, 3.0F, 0.0F, false);
		body.setTextureOffset(0, 12).addBox(-1.0F, -2.0F, 6.0F, 2.0F, 3.0F, 3.0F, 0.0F, false);

		legR1 = new ModelRenderer(this);
		legR1.setRotationPoint(-3.0F, 1.0F, -3.0F);
		body.addChild(legR1);
		legR1.setTextureOffset(10, 12).addBox(0.0F, 0.0F, -1.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		legR2 = new ModelRenderer(this);
		legR2.setRotationPoint(1.0F, 2.0F, -1.0F);
		legR1.addChild(legR2);
		legR2.setTextureOffset(10, 15).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		legL1 = new ModelRenderer(this);
		legL1.setRotationPoint(3.0F, 1.0F, -3.0F);
		body.addChild(legL1);
		legL1.setTextureOffset(10, 12).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		legL2 = new ModelRenderer(this);
		legL2.setRotationPoint(-1.0F, 2.0F, -1.0F);
		legL1.addChild(legL2);
		legL2.setTextureOffset(10, 15).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, -2.0F, -3.0F);
		body.addChild(head);
		head.setTextureOffset(14, 14).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 2.0F, 3.0F, 0.0F, false);
		head.setTextureOffset(21, 0).addBox(-2.0F, 0.0F, -5.0F, 4.0F, 1.0F, 2.0F, 0.0F, false);
		head.setTextureOffset(21, 3).addBox(-2.0F, 1.0F, -5.0F, 4.0F, 1.0F, 2.0F, 0.0F, false);

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.addChild(cube_r1);
		setRotationAngle(cube_r1, -1.0472F, 0.0F, 0.0F);
		cube_r1.setTextureOffset(14, 12).addBox(-3.0F, -2.0F, 0.0F, 6.0F, 2.0F, 0.0F, 0.0F, false);

		body2 = new ModelRenderer(this);
		body2.setRotationPoint(0.0F, 0.0F, 9.0F);
		body.addChild(body2);
		body2.setTextureOffset(0, 0).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 3.0F, 0.0F, false);

		tailL = new ModelRenderer(this);
		tailL.setRotationPoint(2.0F, -1.0F, 0.0F);
		body2.addChild(tailL);
		setRotationAngle(tailL, 0.0F, 0.2618F, 0.0F);
		tailL.setTextureOffset(0, 45).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
		tailL.setTextureOffset(0, 57).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 8.0F, 0.0F, false);

		tailL2 = new ModelRenderer(this);
		tailL2.setRotationPoint(0.0F, 0.0F, 11.0F);
		tailL.addChild(tailL2);
		tailL2.setTextureOffset(0, 66).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);

		clawLL = new ModelRenderer(this);
		clawLL.setRotationPoint(1.0F, 0.0F, 4.0F);
		tailL2.addChild(clawLL);
		setRotationAngle(clawLL, 0.0F, 0.384F, 0.0F);
		clawLL.setTextureOffset(0, 45).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		clawLL2 = new ModelRenderer(this);
		clawLL2.setRotationPoint(0.0F, 0.0F, 2.0F);
		clawLL.addChild(clawLL2);
		clawLL2.setTextureOffset(0, 48).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		clawLL3 = new ModelRenderer(this);
		clawLL3.setRotationPoint(0.0F, 0.0F, 2.0F);
		clawLL2.addChild(clawLL3);
		clawLL3.setTextureOffset(0, 51).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		clawLM = new ModelRenderer(this);
		clawLM.setRotationPoint(0.0F, 0.0F, 4.0F);
		tailL2.addChild(clawLM);
		clawLM.setTextureOffset(13, 45).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		clawLM2 = new ModelRenderer(this);
		clawLM2.setRotationPoint(0.0F, 0.0F, 2.0F);
		clawLM.addChild(clawLM2);
		clawLM2.setTextureOffset(13, 48).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		clawLM3 = new ModelRenderer(this);
		clawLM3.setRotationPoint(0.0F, 0.0F, 2.0F);
		clawLM2.addChild(clawLM3);
		clawLM3.setTextureOffset(13, 51).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		clawLR = new ModelRenderer(this);
		clawLR.setRotationPoint(0.0F, 0.0F, 4.0F);
		tailL2.addChild(clawLR);
		setRotationAngle(clawLR, 0.0F, -0.384F, 0.0F);
		clawLR.setTextureOffset(20, 45).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		clawLR1 = new ModelRenderer(this);
		clawLR1.setRotationPoint(0.0F, 0.0F, 2.0F);
		clawLR.addChild(clawLR1);
		clawLR1.setTextureOffset(20, 48).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		clawLR2 = new ModelRenderer(this);
		clawLR2.setRotationPoint(0.0F, 0.0F, 2.0F);
		clawLR1.addChild(clawLR2);
		clawLR2.setTextureOffset(20, 51).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		tailM = new ModelRenderer(this);
		tailM.setRotationPoint(0.0F, -1.0F, 3.0F);
		body2.addChild(tailM);
		tailM.setTextureOffset(26, 45).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);

		tailM2 = new ModelRenderer(this);
		tailM2.setRotationPoint(0.0F, 0.0F, 9.0F);
		tailM.addChild(tailM2);
		tailM2.setTextureOffset(26, 55).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 9.0F, 0.0F, false);

		clawML = new ModelRenderer(this);
		clawML.setRotationPoint(0.5F, 0.0F, 9.0F);
		tailM2.addChild(clawML);
		setRotationAngle(clawML, 0.0F, 0.3491F, 0.0F);
		clawML.setTextureOffset(26, 45).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		clawML1 = new ModelRenderer(this);
		clawML1.setRotationPoint(0.0F, 0.0F, 2.0F);
		clawML.addChild(clawML1);
		clawML1.setTextureOffset(26, 48).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		clawML2 = new ModelRenderer(this);
		clawML2.setRotationPoint(0.0F, 0.0F, 2.0F);
		clawML1.addChild(clawML2);
		clawML2.setTextureOffset(26, 51).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		clawMM = new ModelRenderer(this);
		clawMM.setRotationPoint(-0.5F, 0.0F, 9.0F);
		tailM2.addChild(clawMM);
		clawMM.setTextureOffset(26, 55).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		clawML4 = new ModelRenderer(this);
		clawML4.setRotationPoint(0.0F, 0.0F, 2.0F);
		clawMM.addChild(clawML4);
		clawML4.setTextureOffset(26, 58).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		clawML5 = new ModelRenderer(this);
		clawML5.setRotationPoint(0.0F, 0.0F, 2.0F);
		clawML4.addChild(clawML5);
		clawML5.setTextureOffset(26, 61).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		clawMR = new ModelRenderer(this);
		clawMR.setRotationPoint(-0.5F, 0.0F, 9.0F);
		tailM2.addChild(clawMR);
		setRotationAngle(clawMR, 0.0F, -0.3491F, 0.0F);
		clawMR.setTextureOffset(38, 55).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		clawMR2 = new ModelRenderer(this);
		clawMR2.setRotationPoint(0.0F, 0.0F, 2.0F);
		clawMR.addChild(clawMR2);
		clawMR2.setTextureOffset(38, 58).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		clawMR3 = new ModelRenderer(this);
		clawMR3.setRotationPoint(0.0F, 0.0F, 2.0F);
		clawMR2.addChild(clawMR3);
		clawMR3.setTextureOffset(38, 61).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		tailR = new ModelRenderer(this);
		tailR.setRotationPoint(-2.0F, -1.0F, 0.0F);
		body2.addChild(tailR);
		setRotationAngle(tailR, 0.0F, -0.2618F, 0.0F);
		tailR.setTextureOffset(49, 35).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
		tailR.setTextureOffset(49, 47).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 8.0F, 0.0F, false);

		tailL4 = new ModelRenderer(this);
		tailL4.setRotationPoint(0.0F, 0.0F, 11.0F);
		tailR.addChild(tailL4);
		tailL4.setTextureOffset(62, 47).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);

		clawLL4 = new ModelRenderer(this);
		clawLL4.setRotationPoint(0.0F, 0.0F, 4.0F);
		tailL4.addChild(clawLL4);
		setRotationAngle(clawLL4, 0.0F, 0.384F, 0.0F);
		clawLL4.setTextureOffset(49, 65).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		clawLL5 = new ModelRenderer(this);
		clawLL5.setRotationPoint(0.0F, 0.0F, 2.0F);
		clawLL4.addChild(clawLL5);
		clawLL5.setTextureOffset(49, 68).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		clawLL6 = new ModelRenderer(this);
		clawLL6.setRotationPoint(0.0F, 0.0F, 2.0F);
		clawLL5.addChild(clawLL6);
		clawLL6.setTextureOffset(49, 71).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		clawLM4 = new ModelRenderer(this);
		clawLM4.setRotationPoint(-1.0F, 0.0F, 4.0F);
		tailL4.addChild(clawLM4);
		clawLM4.setTextureOffset(49, 56).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		clawLM5 = new ModelRenderer(this);
		clawLM5.setRotationPoint(0.0F, 0.0F, 2.0F);
		clawLM4.addChild(clawLM5);
		clawLM5.setTextureOffset(49, 59).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		clawLM6 = new ModelRenderer(this);
		clawLM6.setRotationPoint(0.0F, 0.0F, 2.0F);
		clawLM5.addChild(clawLM6);
		clawLM6.setTextureOffset(49, 62).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		clawLR3 = new ModelRenderer(this);
		clawLR3.setRotationPoint(-1.0F, 0.0F, 4.0F);
		tailL4.addChild(clawLR3);
		setRotationAngle(clawLR3, 0.0F, -0.384F, 0.0F);
		clawLR3.setTextureOffset(55, 56).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		clawLR4 = new ModelRenderer(this);
		clawLR4.setRotationPoint(0.0F, 0.0F, 2.0F);
		clawLR3.addChild(clawLR4);
		clawLR4.setTextureOffset(55, 59).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		clawLR5 = new ModelRenderer(this);
		clawLR5.setRotationPoint(0.0F, 0.0F, 2.0F);
		clawLR4.addChild(clawLR5);
		clawLR5.setTextureOffset(55, 62).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		wingL = new ModelRenderer(this);
		wingL.setRotationPoint(-3.0F, 17.0F, -8.0F);
		wingL.setTextureOffset(33, 6).addBox(-8.0F, 1.0F, 3.0F, 8.0F, 1.0F, 4.0F, 0.0F, false);
		wingL.setTextureOffset(33, 11).addBox(-8.0F, 1.0F, 7.0F, 8.0F, 0.0F, 3.0F, 0.0F, false);
		wingL.setTextureOffset(33, 0).addBox(-8.0F, 0.0F, 0.0F, 8.0F, 3.0F, 3.0F, 0.0F, false);

		wilgL2 = new ModelRenderer(this);
		wilgL2.setRotationPoint(-8.0F, 1.0F, 0.0F);
		wingL.addChild(wilgL2);
		wilgL2.setTextureOffset(33, 23).addBox(-9.0F, 0.0F, 6.0F, 9.0F, 0.0F, 3.0F, 0.0F, false);
		wilgL2.setTextureOffset(32, 14).addBox(-9.0F, 0.0F, 0.0F, 9.0F, 2.0F, 2.0F, 0.0F, false);
		wilgL2.setTextureOffset(32, 18).addBox(-9.0F, 0.0F, 2.0F, 9.0F, 1.0F, 4.0F, 0.0F, false);

		wildL3 = new ModelRenderer(this);
		wildL3.setRotationPoint(-9.0F, 0.0F, 0.0F);
		wilgL2.addChild(wildL3);
		wildL3.setTextureOffset(58, 0).addBox(-9.0F, 0.0F, 0.0F, 9.0F, 1.0F, 4.0F, 0.0F, false);
		wildL3.setTextureOffset(58, 5).addBox(-9.0F, 0.0F, 4.0F, 9.0F, 0.0F, 3.0F, 0.0F, false);

		wingL4 = new ModelRenderer(this);
		wingL4.setRotationPoint(-9.0F, 0.0F, 0.0F);
		wildL3.addChild(wingL4);
		wingL4.setTextureOffset(57, 8).addBox(-5.0F, 0.0F, 0.0F, 5.0F, 1.0F, 2.0F, 0.0F, false);
		wingL4.setTextureOffset(58, 13).addBox(-5.0F, 0.0F, 2.0F, 5.0F, 0.0F, 3.0F, 0.0F, false);

		wingL5 = new ModelRenderer(this);
		wingL5.setRotationPoint(0.0F, 0.0F, 0.0F);
		wingL4.addChild(wingL5);
		wingL5.setTextureOffset(57, 11).addBox(-11.0F, 0.0F, 0.0F, 6.0F, 0.0F, 2.0F, 0.0F, false);

		wingR = new ModelRenderer(this);
		wingR.setRotationPoint(3.0F, 17.0F, -8.0F);
		wingR.setTextureOffset(0, 25).addBox(0.0F, 1.0F, 3.0F, 8.0F, 1.0F, 4.0F, 0.0F, false);
		wingR.setTextureOffset(0, 19).addBox(0.0F, 0.0F, 0.0F, 8.0F, 3.0F, 3.0F, 0.0F, false);
		wingR.setTextureOffset(0, 30).addBox(0.0F, 1.0F, 7.0F, 8.0F, 0.0F, 3.0F, 0.0F, false);

		wingR2 = new ModelRenderer(this);
		wingR2.setRotationPoint(13.0F, 1.0F, 2.0F);
		wingR.addChild(wingR2);
		wingR2.setTextureOffset(0, 42).addBox(-5.0F, 0.0F, 4.0F, 9.0F, 0.0F, 3.0F, 0.0F, false);
		wingR2.setTextureOffset(0, 33).addBox(-5.0F, 0.0F, -2.0F, 9.0F, 2.0F, 2.0F, 0.0F, false);
		wingR2.setTextureOffset(0, 37).addBox(-5.0F, 0.0F, 0.0F, 9.0F, 1.0F, 4.0F, 0.0F, false);

		wingR3 = new ModelRenderer(this);
		wingR3.setRotationPoint(4.0F, 0.0F, -2.0F);
		wingR2.addChild(wingR3);
		wingR3.setTextureOffset(26, 29).addBox(0.0F, 0.0F, 0.0F, 9.0F, 1.0F, 4.0F, 0.0F, false);
		wingR3.setTextureOffset(27, 26).addBox(0.0F, 0.0F, 4.0F, 9.0F, 0.0F, 3.0F, 0.0F, false);

		wingR4 = new ModelRenderer(this);
		wingR4.setRotationPoint(9.0F, 0.0F, 0.0F);
		wingR3.addChild(wingR4);
		wingR4.setTextureOffset(26, 34).addBox(0.0F, 0.0F, 0.0F, 5.0F, 1.0F, 2.0F, 0.0F, false);
		wingR4.setTextureOffset(26, 37).addBox(0.0F, 0.0F, 2.0F, 5.0F, 0.0F, 3.0F, 0.0F, false);

		wingR5 = new ModelRenderer(this);
		wingR5.setRotationPoint(5.0F, 0.0F, 0.0F);
		wingR4.addChild(wingR5);
		wingR5.setTextureOffset(26, 40).addBox(0.0F, 0.0F, 0.0F, 6.0F, 0.0F, 2.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		body.render(matrixStack, buffer, packedLight, packedOverlay);
		wingL.render(matrixStack, buffer, packedLight, packedOverlay);
		wingR.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}