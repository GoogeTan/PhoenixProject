package phoenix.client.models.entity;

 import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

public class CaudaEyesLayer<T extends LivingEntity> extends AbstractEyesLayer<T, CaudaModel<T>> {
    private static final RenderType RENDER_TYPE = RenderType.getEyes(new ResourceLocation("phoenix", "textures/entity/cauda/cauda_eyes.png"));

    public CaudaEyesLayer(IEntityRenderer<T, CaudaModel<T>> rendererIn) {
        super(rendererIn);
    }

    public RenderType getRenderType() {
        return RENDER_TYPE;
    }
}