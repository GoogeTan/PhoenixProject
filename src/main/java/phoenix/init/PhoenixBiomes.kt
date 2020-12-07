package phoenix.init

import net.minecraft.world.biome.Biome
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix
import phoenix.utils.GenerationUtils
import phoenix.world.biomes.UnderBiome
import phoenix.world.builders.Builders

object PhoenixBiomes
{
    @JvmStatic val BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, Phoenix.MOD_ID)

    @JvmStatic val UNDER     = BIOMES.register("under", ::UnderBiome)
    @JvmStatic val HEARTVOID = BIOMES.register("heart_void", ::HeartVoidBiome)

    fun register()
    {
        BIOMES.register(FMLJavaModLoadingContext.get().modEventBus)
    }
}

class HeartVoidBiome : Biome(GenerationUtils.defaultSettingsForEnd(Builders.HEARTVOID, Builders.HEARTVOID_CONFIG))