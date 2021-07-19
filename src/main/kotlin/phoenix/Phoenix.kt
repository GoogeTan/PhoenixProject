package phoenix

import net.minecraft.block.Blocks
import net.minecraft.item.ItemGroup
import net.minecraft.world.biome.FuzzedBiomeMagnifier
import net.minecraft.world.dimension.DimensionType
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import phoenix.init.*
import phoenix.init.PhxConfiguration.Common
import phoenix.utils.client
import phoenix.world.EndDimension

@Mod(Phoenix.MOD_ID)
class Phoenix
{
    init
    {
        PhxEnchantments.register()
        PhxBiomes.register()
        PhxBlocks.register()
        PhxTiles.register()
        PhxFeatures.register()
        PhxEntities.register()
        PhxItems.register()
        PhxContainers.register()
        PhxRecipeSerializers.register()
        PhxTriggers.register()
        PhxPotions.register()
        PhxFluids.register()
        val specPair = ForgeConfigSpec.Builder().configure(::Common)
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, specPair.right)
        PhxConfiguration.commonConfig = specPair.left

        client { _, _, _ -> StaticInit.init() }
    }

    companion object
    {
        const val MOD_ID = "phoenix"

        val ASH : ItemGroup = PhoenixGroup("$MOD_ID.ash", Blocks.END_PORTAL_FRAME)
        val REDO: ItemGroup = PhoenixGroup("$MOD_ID.redo", PhxBlocks::seta)

        init
        {
            DimensionType.THE_END = DimensionType.register("the_end", DimensionType(2, "_end", "DIM1", ::EndDimension, false, FuzzedBiomeMagnifier.INSTANCE))
            PhxLootTables.init()
        }
    }
}