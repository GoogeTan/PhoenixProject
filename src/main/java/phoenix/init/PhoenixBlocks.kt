package phoenix.init

import net.minecraft.block.material.Material
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix
import phoenix.blocks.AntiAirBlock
import phoenix.blocks.UpdaterBlock
import phoenix.blocks.ash.EndStoneColumnBlock
import phoenix.blocks.ash.OvenBlock
import phoenix.blocks.ash.PotteryBarrelBlock
import phoenix.blocks.ash.ZirconiumOreBlock
import phoenix.blocks.redo.*
import phoenix.utils.block.AnonimBlock

object PhoenixBlocks
{
    @JvmStatic
    val BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Phoenix.MOD_ID)!!

    val UPDATER           = BLOCKS.register("updater",           ::UpdaterBlock)!!
    val PIPE              = BLOCKS.register("pipe",              ::PipeBlock)!!
    val TANK              = BLOCKS.register("tank",              ::TankBlock)!!
    val FERTILE_END_STONE = BLOCKS.register("fertile_end_stone", ::FertileEndStoneBlock)!!
    val KIKIN_STEAM       = BLOCKS.register("kikin_stem",        ::KikinStemBlock)!!
    val KIKIN_FRUIT       = BLOCKS.register("kikin_fruit",       ::KikiNFruitBlock)!!
    val ANTI_AIR          = BLOCKS.register("anti_air",          ::AntiAirBlock)!!
    val POTTERY_BARREL    = BLOCKS.register("pottery_barrel",    ::PotteryBarrelBlock)!!
    val END_STONE_COLUMN  = BLOCKS.register("end_stone_column",  ::EndStoneColumnBlock)!!
    val OVEN              = BLOCKS.register("oven",              ::OvenBlock)!!
    val ZIRCONIUM         = BLOCKS.register("zirconium_ore",     ::ZirconiumOreBlock)!!
    val TEXT_BLOCK        = BLOCKS.register("block_with_text", AnonimBlock.create(Material.ROCK))!!

    fun register()
    {
        BLOCKS.register(FMLJavaModLoadingContext.get().modEventBus)
    }
}