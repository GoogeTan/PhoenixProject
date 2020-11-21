package phoenix

import net.minecraft.item.ItemGroup
import net.minecraft.world.World
import net.minecraft.world.biome.FuzzedBiomeMagnifier
import net.minecraft.world.dimension.DimensionType
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import org.apache.logging.log4j.LogManager
import phoenix.init.*
import phoenix.init.PhoenixConfiguration.Common
import phoenix.world.EndBiomedDimension

@Mod(Phoenix.MOD_ID)
class Phoenix
{
    init
    {
        PhoenixBiomes.register()
        PhoenixBlocks.register()
        PhoenixTiles.register()
        PhoenixFeatures.register()
        PhoenixEntities.register()
        PhoenixItems.register()
        PhoenixContainers.register()
        PhoenixRecipeSerializers.register()

        val specPair = ForgeConfigSpec.Builder().configure { builder: ForgeConfigSpec.Builder -> Common(builder) }
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, specPair.right)
        PhoenixConfiguration.COMMON_CONFIG = specPair.left
    }


    companion object
    {

        public const val MOD_ID = "phoenix"
        @JvmStatic
        val LOGGER = LogManager.getLogger()
        @JvmStatic
        val PHOENIX: ItemGroup = PhoenixGroup(MOD_ID)

        init
        {
            DimensionType.THE_END = DimensionType.register("the_end",
                    DimensionType(2, "_end", "DIM1", { world: World?, type: DimensionType? -> EndBiomedDimension(world, type) }, false, FuzzedBiomeMagnifier.INSTANCE))
        }
    }
}