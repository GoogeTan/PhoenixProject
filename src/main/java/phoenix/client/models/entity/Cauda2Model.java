package phoenix.client.models.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import phoenix.Phoenix;
import phoenix.init.PhoenixShaiders;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;

public class Cauda2Model<T extends LivingEntity> extends EntityModel<T>
{
	private final ModelRenderer body;
	private final ModelRenderer bb_main;

	public Cauda2Model() {
		textureWidth = 16;
		textureHeight = 16;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 19.0F, -5.0F);
		body.setTextureOffset(0, 0).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 3.0F, 9.0F, 0.0F, false);
		body.setTextureOffset(0, 0).addBox(-3.0F, 1.0F, -4.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);

		bb_main = new ModelRenderer(this);
		bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
		bb_main.setTextureOffset(0, 0).addBox(2.0F, -4.0F, -9.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		int programID = PhoenixShaiders.getProgram("normal");
		ARBShaderObjects.glUseProgramObjectARB(programID);
		//Остальное не обязательно, это параметры шейдера
		GL13.glActiveTexture(GL_TEXTURE1);
		GL11.glBindTexture(GL_TEXTURE_2D, Minecraft.getInstance().getFramebuffer().framebufferTexture);

		int framebufferTexture = GL20.glGetUniformLocation(programID, "fbTexture");
		GL20.glUniform1i(framebufferTexture, 1);

		body.render(matrixStack, buffer, packedLight, packedOverlay);
		bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}