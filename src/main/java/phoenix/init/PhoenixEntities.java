package phoenix.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import phoenix.Phoenix;
import phoenix.enity.TalpaEntity;

public class PhoenixEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Phoenix.MOD_ID);

    public static final RegistryObject<EntityType<TalpaEntity>> TALPA = ENTITIES.register("talpa", () -> make(new ResourceLocation(Phoenix.MOD_ID, "talpa"),
            TalpaEntity::new, EntityClassification.CREATURE));


    private static <E extends Entity> EntityType<E> make(ResourceLocation id, EntityType.IFactory<E> factory, EntityClassification classification)
    {
        return EntityType.Builder.create(factory, classification)
                .size(0.6F, 1.8F)
                .setTrackingRange(80)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true)
                .build(id.toString());
    }

    public static void register()
    {
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
