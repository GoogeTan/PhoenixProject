package phoenix.init

import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.common.ForgeConfigSpec.EnumValue
import net.minecraftforge.common.ForgeConfigSpec.IntValue
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.config.ModConfig.Reloading
import phoenix.Phoenix


object PhoenixConfiguration
{
    lateinit var COMMON_CONFIG: Common

    //On reload
    fun onReload()
    {
    }

    @SubscribeEvent
    fun onConfigChanged(event: Reloading)
    {
        if (event.config.modId == Phoenix.MOD_ID)
        {
            onReload()
            Phoenix.LOGGER.debug("Reloaded")
        }
    }

    class Common(builder: ForgeConfigSpec.Builder)
    {
        var HARDCORE: EnumValue<GameMode>
        var GENERATE_OPTIONAL_STRUCTURES: ForgeConfigSpec.BooleanValue
        var BIOME_SIZE: IntValue

        init
        {
            builder.push("Game Settings") //  comment("Settings that are not reversible without consequences.").
            HARDCORE = builder.worldRestart().comment("Is game in hardcode mode or easy mode. If \"Liahim\" game will be hard and trolling like Misty World or higher" +
                    ", but if \"hohserg\" it will be easier and more simple.")
                    .defineEnum("Game mode", GameMode.normal)
            GENERATE_OPTIONAL_STRUCTURES = builder.worldRestart().define("Is generating optional structures", true)
            BIOME_SIZE = builder.worldRestart().defineInRange("Biome size", 6, 1, 15)
            builder.pop()
        }
    }

    enum class GameMode(var maxKnifeUsages: Int)
    {
        normal(60), Liahim(16), hohserg(80);
    }
}