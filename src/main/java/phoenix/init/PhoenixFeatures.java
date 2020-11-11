package phoenix.init;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import phoenix.Phoenix;
import phoenix.world.structures.ErasedStructure;
import phoenix.world.structures.corn.CornStructure;

public class PhoenixFeatures
{
    public static final DeferredRegister<Feature<?>> FEATURES = new DeferredRegister(ForgeRegistries.FEATURES, Phoenix.MOD_ID);

    public static final RegistryObject<Structure<NoFeatureConfig>> ERASED = FEATURES.register("erased", ErasedStructure::new);
    public static final RegistryObject<Structure<NoFeatureConfig>> CORN   = FEATURES.register("corn",     CornStructure::new);


    public static void register()
    {
        FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}