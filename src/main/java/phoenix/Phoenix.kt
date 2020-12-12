package phoenix

import net.minecraft.block.Blocks
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemGroup
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.FuzzedBiomeMagnifier
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.IFeatureConfig
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityInject
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.items.IItemHandler
import org.apache.logging.log4j.LogManager
import phoenix.init.*
import phoenix.init.PhoenixConfiguration.Common
import phoenix.utils.capablity.IChapterReader
import phoenix.world.EndDimension

@Mod(Phoenix.MOD_ID)
class Phoenix
{
    init
    {
        PhoenixEnchantments.register()
        PhoenixBiomes.register()
        PhoenixBlocks.register()
        PhoenixTiles.register()
        PhoenixFeatures.register()
        PhoenixEntities.register()
        PhoenixItems.register()
        PhoenixContainers.register()
        PhoenixRecipeSerializers.register()
        val specPair = ForgeConfigSpec.Builder().configure(::Common)
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, specPair.right)
        PhoenixConfiguration.COMMON_CONFIG = specPair.left
    }

    companion object
    {
        @CapabilityInject(IChapterReader::class)
        lateinit var ITEM_HANDLER_CAPABILITY: Capability<IChapterReader>
        const val MOD_ID = "phoenix"
        @JvmStatic
        val LOGGER = LogManager.getLogger()!!
        @JvmStatic
        val ASH: ItemGroup = PhoenixGroup("$MOD_ID.ash", Blocks.END_PORTAL_FRAME)
        @JvmStatic
        val REDO: ItemGroup = PhoenixGroup("$MOD_ID.redo",Blocks.END_PORTAL_FRAME) //PhoenixBlocks.UPDATER)

        init
        {
            DimensionType.THE_END = DimensionType.register("the_end", DimensionType(2, "_end", "DIM1", ::EndDimension, false, FuzzedBiomeMagnifier.INSTANCE))
            PhoenixLootTables.init()
        }
    }
}