package phoenix;

import net.minecraft.item.ItemGroup;
import net.minecraft.world.biome.FuzzedBiomeMagnifier;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import phoenix.init.*;
import phoenix.world.EndBiomedDimension;

@Mod(Phoenix.MOD_ID)
public class Phoenix
{
    public static final String MOD_ID = "phoenix";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final ItemGroup PHOENIX = new PhoenixGroup(Phoenix.MOD_ID);
    public static Phoenix instance;

    public Phoenix()
    {
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
        PhoenixBiomes     .register();
        PhoenixBlocks     .register();
        PhoenixTiles      .register();
        PhoenixFeatures   .register();
        PhoenixEntities   .register();
        PhoenixItems      .register();
        PhoenixContainers .register();
        Pair<PhoenixConfiguration.Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(PhoenixConfiguration.Common::new);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, specPair.getRight());
        PhoenixConfiguration.COMMON_CONFIG = specPair.getLeft();
    }

    static
    {
        DimensionType.THE_END = DimensionType.register("the_end",
                new DimensionType(2, "_end", "DIM1",
                        EndBiomedDimension::new, false, FuzzedBiomeMagnifier.INSTANCE));
    }
}