package phoenix.init

import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.common.ForgeConfigSpec.EnumValue
import net.minecraftforge.common.ForgeConfigSpec.IntValue

object PhxConfiguration
{
    val caudaInventorySize : Int
        get() = commonConfig.gameMode.get().caudaInventorySize
    val gameMode : GameMode
        get() = commonConfig.gameMode.get()
    val biomeSize : Int
        get() = commonConfig.biomeSize.get()
    val isDebugMode : Boolean
        get() = commonConfig.debug.get()

    lateinit var commonConfig: Common

    class Common(builder: ForgeConfigSpec.Builder)
    {
        var gameMode : EnumValue<GameMode>
        var biomeSize : IntValue
        var debug : ForgeConfigSpec.BooleanValue

        init
        {
            builder.push("Game Settings")
                gameMode = builder.worldRestart().comment("If \"Liahim\" game will be hard and trolling like Misty World or higher, but if \"Hohserg\" it will be easier and more simple.").defineEnum("Game mode", GameMode.Normaly)
                biomeSize = builder.worldRestart().defineInRange("Biome size", 6, 1, 15)
            builder.pop()
            builder.push("Debug settings")
                debug = builder.worldRestart().define("is debug mode", false)
            builder.pop()
        }
    }

    enum class GameMode(val maxKnifeUsages: Int, val caudaInventorySize : Int, val textureSuffix : String)
    {
        Normaly(60, 20, ""), Liahim(16, 9, "_liahim"), Hohserg(80, 20, "_hohserg");
    }
}