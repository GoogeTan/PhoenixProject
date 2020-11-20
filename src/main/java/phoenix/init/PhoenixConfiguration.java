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
            HARDCORE = builder.
                    worldRestart().
                    comment("Is game in hardcode mode or easy mode. If \"Liahim\" game will be hard and trolling like Misty World or higher" +
                            ", but if \"hohserg\" it will be easier and more simple.")
                    .defineEnum("Game mode", GameMode.normal);
            GENERATE_OPTIONAL_STRUCTURES = builder.
                    worldRestart().
                    define("Is generating optional structures", true);
            BIOME_SIZE = builder.
                    worldRestart().defineInRange("Biome size", 6, 1, 15);
            builder.pop();
        }

        public ForgeConfigSpec.EnumValue<GameMode> HARDCORE;
        public ForgeConfigSpec.BooleanValue GENERATE_OPTIONAL_STRUCTURES;
        public ForgeConfigSpec.IntValue     BIOME_SIZE;
    }
    public enum GameMode
    {
        normal(60),
        Liahim(16),
        hohserg(80);
        public int maxKnifeUsages;
        GameMode(int maxKnifeUsagesIn)
        {
            maxKnifeUsages = maxKnifeUsagesIn;
        }
    }
    //On reload
    public static void onReload()
    {
    }


    @SubscribeEvent
    public static void onConfigChanged(ModConfig.Reloading event)
    {
        if (event.getConfig().getModId().equals(Phoenix.MOD_ID))
        {
            onReload();
            Phoenix.LOGGER.debug("Reloaded");
        }
    }
}