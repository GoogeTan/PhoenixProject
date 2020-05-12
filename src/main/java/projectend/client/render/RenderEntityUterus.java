package projectend.client.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import projectend.Projectend;
import projectend.entity.unit02.EntityUterus;
import projectend.entity.unit02.ModelIUterus;

import javax.annotation.Nonnull;

public class RenderEntityUterus extends RenderLiving<EntityUterus>
{
    /*
    Конструктор рендера,
    теперь о super:
        1 параметр - наш RenderManager,
        2 параметр - наша модель,
        3 параметр - размер тени(стандартно 0.5F)
    */
    public RenderEntityUterus(RenderManager manager)
    {
        super(manager, new ModelIUterus(), 0.1F);
    }
    public static Factory FACTORY = new Factory();

    @Override
    @Nonnull /*Возвращаем текстуру моба*/
    protected ResourceLocation getEntityTexture(@Nonnull EntityUterus entity)
    {
        return new ResourceLocation(Projectend.MOD_ID, "textures/entity/beacon.png");
    }
    /*--------->НАШ РЕНДЕР ФЭКТОРИ <---------*/
    public static class Factory implements IRenderFactory<EntityUterus>
    {
        @Override
        public Render<? super EntityUterus> createRenderFor(RenderManager manager) {
            /*И наконец-то из всего этого создаём рендер*/
            return new RenderEntityUterus(manager);
        }
    }
}
