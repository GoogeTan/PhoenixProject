package phoenix.init

import net.minecraft.world.biome.Biome
import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix
import phoenix.utils.GenerationUtils
import phoenix.world.biomes.SmallIslandsUnderBiome
import phoenix.world.biomes.UnderBiome
import phoenix.world.builders.Builders
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhxBiomes
{
    val BIOMES = KDeferredRegister(ForgeRegistries.BIOMES, Phoenix.MOD_ID)

    val UNDER               by BIOMES.register("under")      { UnderBiome }
    val HEARTVOID           by BIOMES.register("heart_void") { object : Biome(GenerationUtils.defaultSettingsForEnd(Builders.HEARTVOID, Builders.HEARTVOID_CONFIG)){} }
    val SMALL_ISLANDS_UNDER by BIOMES.register("small_islands_under")      { SmallIslandsUnderBiome }

    fun register() =  BIOMES.register(MOD_BUS)
}