package phoenix.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import phoenix.Phoenix;
import phoenix.client.models.entity.Cauda2Model;
import phoenix.client.models.entity.CaudaEyesLayer;
import phoenix.client.models.entity.CaudaModel;
import phoenix.enity.CaudaEntity;

import javax.annotation.Nonnull;

public class CaudaRenderer extends MobRenderer<CaudaEntity, Cauda2Model<CaudaEntity>>
{
    public CaudaRenderer(EntityRendererManager renderManager)
    {
        super(renderManager, new Cauda2Model(), 0.5F);
        this.addLayer(new CaudaEyesLayer<>(this));
    }


    @Nonnull
    @Override
    public ResourceLocation getEntityTexture(CaudaEntity entity)
    {
        return entity.isChild() ?
                new ResourceLocation(Phoenix.MOD_ID, "textures/entity/cauda/cauda_child.png")
                    :
                new ResourceLocation(Phoenix.MOD_ID, "textures/entity/cauda/cauda.png");
    }

    @Override
    protected void applyRotations(CaudaEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks)
    {
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(entityLiving.rotationPitch));
    }

    @Override
    protected void preRenderCallback(CaudaEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime)
    {
        int size = entitylivingbaseIn.getCaudaSize();
        float scale = 1.0F + 0.15F * (float)size;
        matrixStackIn.scale(scale, scale, scale);
        //matrixStackIn.translate(0.0D, 1.3125D, 0.1875D);
        //matrixStackIn.rotate(new Quaternion(0, 90,0, true));
    }
}

