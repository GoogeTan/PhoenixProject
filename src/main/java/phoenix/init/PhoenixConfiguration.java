package phoenix.init;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import phoenix.Phoenix;

@Mod.EventBusSubscriber(modid = Phoenix.MOD_ID)
public class PhoenixConfiguration
{
    public static Common COMMON_CONFIG;

    public static class Common
    {
        public Common(ForgeConfigSpec.Builder builder)
        {
            builder.
                  //  comment("Settings that are not reversible without consequences.").
                    push("Game Settings");
            IS_HARDCORE = builder.
                    worldRestart().
                    comment("Is game in hardcode mode. It will be hard like Misty World or higher").
                    define("Is Liahim mode", true);
            GENERATE_OPTIONAL_STRUCTURES = builder.
                    worldRestart().
                    define("Is generating optional structures", true);
            BIOME_SIZE = builder.
                    worldRestart().defineInRange("Biome size", 6, 1, 15);
            builder.pop();
        }

        public ForgeConfigSpec.BooleanValue IS_HARDCORE;
        public ForgeConfigSpec.BooleanValue GENERATE_OPTIONAL_STRUCTURES;
        public ForgeConfigSpec.IntValue     BIOME_SIZE;
    }

    //On reload
    public static void build()
    {
    }


    @SubscribeEvent
    public static void onConfigChanged(ModConfig.Reloading event)
    {
        if (event.getConfig().getModId().equals(Phoenix.MOD_ID))
        {
            build();
            Phoenix.LOGGER.debug("Reloaded");
        }
    }
}