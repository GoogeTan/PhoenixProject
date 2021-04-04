package phoenix.init

import net.minecraft.world.biome.Biome
import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix
import phoenix.utils.GenerationUtils
import phoenix.world.biomes.UnderBiome
import phoenix.world.builders.Builders
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhoenixBiomes
{
    val BIOMES = KDeferredRegister(ForgeRegistries.BIOMES, Phoenix.MOD_ID)

    val UNDER     by BIOMES.register("under", ::UnderBiome)
    val HEARTVOID by BIOMES.register("heart_void", ::HeartVoidBiome)

    fun register() =  BIOMES.register(MOD_BUS)
}

class HeartVoidBiome : Biome(GenerationUtils.defaultSettingsForEnd(Builders.HEARTVOID, Builders.HEARTVOID_CONFIG))