package phoenix.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import phoenix.Phoenix;
import phoenix.client.models.entity.Cauda2Model;
import phoenix.client.models.entity.CaudaEyesLayer;
import phoenix.enity.CaudaEntity;

import javax.annotation.Nonnull;

public class CaudaRenderer extends MobRenderer<CaudaEntity, Cauda2Model<CaudaEntity>>
{
    public CaudaRenderer(EntityRendererManager renderManager)
    {
        super(renderManager, new Cauda2Model<>(), 1.5F);
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
    protected void applyRotations(@NotNull CaudaEntity entity, @NotNull MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks)
    {
        super.applyRotations(entity, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(entity.rotationPitch));
    }

    @Override
    protected void preRenderCallback(CaudaEntity entity, MatrixStack matrixStackIn, float partialTickTime)
    {
        int size = entity.getCaudaSize();
        float scale = 1.0F + 0.25F * (float)size;
        matrixStackIn.scale(scale, scale, scale);
    }
}

