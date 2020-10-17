package phoenix.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import phoenix.Phoenix;
import phoenix.client.models.entity.CaudaEyesLayer;
import phoenix.client.models.entity.CaudaModel;
import phoenix.enity.CaudaEntity;

import javax.annotation.Nonnull;

public class CaudaRenderer extends MobRenderer<CaudaEntity, CaudaModel<CaudaEntity>>
{
    public CaudaRenderer(EntityRendererManager renderManager)
    {
        super(renderManager, new CaudaModel(), 0.5F);
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
}

