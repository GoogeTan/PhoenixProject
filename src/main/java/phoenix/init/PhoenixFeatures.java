package phoenix.init;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import phoenix.Phoenix;
import phoenix.world.structures.CustomEndSpike;
import phoenix.world.structures.remains.RemainsStructure;

public class PhoenixFeatures
{
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Phoenix.MOD_ID);

    public static final RegistryObject<Structure<NoFeatureConfig>> REMAINS     = FEATURES.register("remains",     RemainsStructure::new);
    public static final RegistryObject<CustomEndSpike>             END_SPIKE   = FEATURES.register("new_end_spike", CustomEndSpike::new);

    public static void register()
    {
        FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}