package me.zahara.phoenix

import me.zahara.phoenix.init.PhxBiomes
import me.zahara.phoenix.init.PhxBlocks
import me.zahara.phoenix.init.PhxSurfaceBuilders
import me.zahara.phoenix.world.DecayBiomeSource
import me.zahara.phoenix.world.DecayChunkGenerator
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.loading.FMLConfig

internal const val modId = "phoenix"

@Mod(modId)
class Phoenix
{
    init
    {
        PhxBiomes.register()
        PhxBlocks.register()
        FMLConfig.defaultConfigPath()
        PhxSurfaceBuilders.register()
        Registry.register(Registry.CHUNK_GENERATOR, ResourceLocation(modId, "decay"), DecayChunkGenerator.CODEC)
        Registry.register(Registry.BIOME_SOURCE, ResourceLocation(modId, "decay"), DecayBiomeSource.CODEC)
    }

    companion object
    {
        val creativeModeTab = object : CreativeModeTab("phoenix") {
            override fun makeIcon(): ItemStack = ItemStack(PhxBlocks.deepStone)
        }
    }
}