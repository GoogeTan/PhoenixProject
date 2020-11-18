package phoenix.client.models.entity;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CaudaEyesLayer<T extends LivingEntity> extends AbstractEyesLayer<T, Cauda2Model<T>> {
    private static final RenderType RENDER_TYPE = RenderType.getEyes(new ResourceLocation("phoenix", "textures/entity/cauda/cauda_eyes.png"));

    public CaudaEyesLayer(IEntityRenderer<T, Cauda2Model<T>> rendererIn) {
        super(rendererIn);
    }

    @NotNull
    public RenderType getRenderType() {
        return RENDER_TYPE;
    }
}