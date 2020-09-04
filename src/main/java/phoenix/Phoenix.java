package phoenix;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.biome.FuzzedBiomeMagnifier;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import phoenix.init.*;
import phoenix.world.EndBiomedDimension;

@Mod(Phoenix.MOD_ID)
@Mod.EventBusSubscriber(modid = Phoenix.MOD_ID)
public class Phoenix
{
    public static final String MOD_ID = "phoenix";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final ItemGroup PHOENIX = new PhoenixGroup(Phoenix.MOD_ID, () -> new ItemStack(Items.END_PORTAL_FRAME));
    public static Phoenix instance;

    public Phoenix()
    {
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
        PhoenixBiomes  .register();
        PhoenixBlocks  .register();
        PhoenixTile    .register();
        PhoenixFeatures.register();
        PhoenixEntities.register();
    }

    static
    {
        DimensionType.THE_END = DimensionType.register("the_end",
                new DimensionType(2, "_end", "DIM1",
                        EndBiomedDimension::new, false, FuzzedBiomeMagnifier.INSTANCE));
    }
}