package phoenix

import com.mojang.datafixers.Dynamic
import net.minecraft.item.ItemGroup
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import net.minecraft.world.biome.FuzzedBiomeMagnifier
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.gen.feature.EndSpikeFeatureConfig
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.IFeatureConfig
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import org.apache.logging.log4j.LogManager
import phoenix.init.*
import phoenix.init.PhoenixConfiguration.Common
import phoenix.world.EndDimension
import phoenix.world.structures.CustomEndSpike

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
        val specPair = ForgeConfigSpec.Builder().configure { builder: ForgeConfigSpec.Builder -> Common(builder) }
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, specPair.right)
        PhoenixConfiguration.COMMON_CONFIG = specPair.left
    }


    companion object
    {
        const val MOD_ID = "phoenix"
        @JvmStatic
        val LOGGER = LogManager.getLogger()
        @JvmStatic
        val PHOENIX: ItemGroup = PhoenixGroup(MOD_ID)
        @JvmStatic
        public var customEndSpike = register("new_end_spike", CustomEndSpike { d: Dynamic<*> -> EndSpikeFeatureConfig.deserialize(d) })
        init
        {
            DimensionType.THE_END = DimensionType.register("the_end", DimensionType(2, "_end", "DIM1", { world: World, type: DimensionType -> EndDimension(world, type) }, false, FuzzedBiomeMagnifier.INSTANCE))
            //Feature.END_SPIKE = register("new_end_spike", CustomEndSpike { d: Dynamic<*> -> EndSpikeFeatureConfig.deserialize(d) })
            PhoenixSounds.init()
            PhoenixLootTables.init()
        }

        fun <C : IFeatureConfig, F : Feature<C>> register(key: String, value : F): F
        {
            return Registry.register<Feature<*>>(Registry.FEATURE, key, value) as F
        }
    }
}