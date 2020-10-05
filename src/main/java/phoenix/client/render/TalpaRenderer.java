package phoenix.client.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import phoenix.Phoenix;
import phoenix.client.models.TalpaModel;
import phoenix.enity.TalpaEntity;

import javax.annotation.Nonnull;

public class TalpaRenderer extends MobRenderer<TalpaEntity, TalpaModel>
{
    public TalpaRenderer(EntityRendererManager renderManager)
    {
        super(renderManager, new TalpaModel(), 0.5F);
    }

      
    @Override
    public ResourceLocation getEntityTexture(   TalpaEntity entity)
    {
        return entity.isChild() ? new ResourceLocation(Phoenix.MOD_ID, "textures/entity/talpa_child.png") :
                                  new ResourceLocation(Phoenix.MOD_ID, "textures/entity/talpa.png");
    }
}
