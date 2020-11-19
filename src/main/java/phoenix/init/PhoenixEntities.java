package phoenix.init;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import phoenix.Phoenix;
import phoenix.enity.CaudaEntity;
import phoenix.enity.TalpaEntity;

public class PhoenixEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Phoenix.MOD_ID);

    public static final RegistryObject<EntityType<TalpaEntity>> TALPA = ENTITIES.register("talpa",
            () ->
                    EntityType.Builder.create(TalpaEntity::new, EntityClassification.CREATURE)
                            .size(0.5F, 0.5F)
                            .setTrackingRange(80)
                            .setUpdateInterval(3)
                            .setShouldReceiveVelocityUpdates(true)
                            .size(0.5F, 0.5F)
                            .build(new ResourceLocation(Phoenix.MOD_ID, "talpa").toString()));

    public static final RegistryObject<EntityType<CaudaEntity>> CAUDA = ENTITIES.register("cauda",
            () ->
                    EntityType.Builder.create(CaudaEntity::new, EntityClassification.CREATURE)
                            .size(0.9F, 0.5F)
                            .setTrackingRange(80)
                            .setUpdateInterval(3)
                            .setShouldReceiveVelocityUpdates(true)
                            .size(0.5f, 0.5f)
                            .build(new ResourceLocation(Phoenix.MOD_ID, "cauda").toString()));


    public static void register()
    {
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
