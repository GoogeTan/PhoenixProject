package phoenix.init;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import phoenix.Phoenix;
import phoenix.world.structures.ErasedStructure;

import java.util.Locale;

public class PhoenixFeatures
{
    public static final DeferredRegister<Feature<?>> FEATURES = new DeferredRegister(ForgeRegistries.FEATURES, Phoenix.MOD_ID);

    public static final RegistryObject<Structure<NoFeatureConfig>> ERASED = FEATURES.register("erased", ErasedStructure::new);

    public static void register()
    {
        FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}