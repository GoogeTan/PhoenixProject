package phoenix.init

import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.common.ForgeConfigSpec.EnumValue
import net.minecraftforge.common.ForgeConfigSpec.IntValue

object PhxConfiguration
{
    val caudaInventorySize : Int
        get() = COMMON_CONFIG.gameMode.get().caudaInventorySize
    val gameMode : GameMode
        get() = COMMON_CONFIG.gameMode.get()
    val biomeSize : Int
        get() = COMMON_CONFIG.BIOME_SIZE.get()
    val isDebugMode : Boolean
        get() = COMMON_CONFIG.debug.get()
    fun getPipeCapacity(tier : Int) = COMMON_CONFIG.PIPE_CAPACITIES.get()[tier]
    fun getTransferRate(tier : Int) = COMMON_CONFIG.PIPE_CAPACITIES.get()[tier]

    lateinit var COMMON_CONFIG: Common

    class Common(builder: ForgeConfigSpec.Builder)
    {
        var gameMode : EnumValue<GameMode>
        var BIOME_SIZE : IntValue
        var PIPE_CAPACITIES : ForgeConfigSpec.ConfigValue<List<Int>>
        var debug : ForgeConfigSpec.BooleanValue

        init
        {
            builder.push("Game Settings")
            gameMode = builder.worldRestart().comment("If \"Liahim\" game will be hard and trolling like Misty World or higher" +
                    ", but if \"hohserg\" it will be easier and more simple.")
                    .defineEnum("Game mode", GameMode.normal)
            BIOME_SIZE = builder.worldRestart().defineInRange("Biome size", 6, 1, 15)
            builder.pop()
            builder.push("Debug settings")
            debug = builder.worldRestart().define("is debug mode", false)
            builder.pop()
            builder.push("Pipe settings")
            PIPE_CAPACITIES = builder.comment("List of pipe capacities. ") .define("Values", arrayListOf(1000, 2000, 4000, 8000));
            builder.pop()
        }
    }

    enum class GameMode(val maxKnifeUsages: Int, val caudaInventorySize : Int, val textureSuffix : String)
    {
        normal(60, 20, ""), Liahim(16, 9, "_liahim"), hohserg(80, 20, "_hohserg");
    }
}