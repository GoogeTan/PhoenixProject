package phoenix.init;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import phoenix.Phoenix;
import phoenix.world.biomes.HeartVoidBiome;
import phoenix.world.biomes.UnderBiome;

public class PhoenixBiomes
{
    private static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, Phoenix.MOD_ID);

    public static final RegistryObject<Biome> UNDER     = BIOMES.register("under", UnderBiome::new);
    public static final RegistryObject<Biome> HEARTVOID = BIOMES.register("heart_void", HeartVoidBiome::new);

    public static void register()
    {
        BIOMES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
