package phoenix.init

import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.fluid.FlowingFluid
import net.minecraft.item.ItemStack
import net.minecraft.world.storage.loot.LootContext
import net.minecraftforge.common.ToolType
import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix
import phoenix.blocks.UpdaterBlock
import phoenix.blocks.ash.OvenBlock
import phoenix.blocks.ash.PotteryBarrelBlock
import phoenix.blocks.redo.*
import phoenix.tile.TextTile
import phoenix.utils.block.AnonimBlock
import phoenix.utils.block.INonItem
import phoenix.utils.block.INonTab
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhoenixBlocks
{
    val BLOCKS = KDeferredRegister(ForgeRegistries.BLOCKS, Phoenix.MOD_ID)

    val UPDATER           by  BLOCKS.register("updater"          ) { UpdaterBlock         }
    val PIPE              by  BLOCKS.register("pipe",              ::PipeBlock            )
    val TANK              by  BLOCKS.register("tank"             ) { TankBlock            }
    val FERTILE_END_STONE by  BLOCKS.register("fertile_end_stone") { FertileEndStoneBlock }
    val ANTI_AIR          by  BLOCKS.register("anti_air"         ) { AntiAirBlock         }
    val POTTERY_BARREL    by  BLOCKS.register("pottery_barrel",    ::PotteryBarrelBlock   )
    val ELECTRIC_BARREL   by  BLOCKS.register("electric_barrel",   ::ElectricBarrelBlock  )
    val END_STONE_COLUMN  by  BLOCKS.register("end_stone_column" ) { EndStoneColumnBlock  }
    val OVEN              by  BLOCKS.register("oven",              ::OvenBlock            )
    val SETA              by  BLOCKS.register("seta"             ) { SetaBlock            }
    val ZIRCONIUM         by  BLOCKS.register("zirconium_ore"    ) { ZirconiumOreBlock    }
    val TEXT_BLOCK        by  BLOCKS.register("block_with_text"  ) { AnonimBlock.create(Material.ROCK, ::TextTile, Phoenix.REDO) }
    val ARMORED_GLASS     by  BLOCKS.register("armored_glass"    ) { ArmoredGlassBlock    }
    //val FRAGILE_BLOCK    : RegistryObject<Block> = BLOCKS.register("fragile_block", ::FragileBlock)!!

    fun register() = BLOCKS.register(MOD_BUS)
}

object ZirconiumOreBlock : OreBlock(Properties.create(Material.ROCK).hardnessAndResistance(3f).harvestTool(ToolType.PICKAXE))
{
    override fun getDrops(state: BlockState, builder: LootContext.Builder) = listOf(ItemStack(this))
}

object EndStoneColumnBlock : RotatedPillarBlock(Properties.create(Material.ROCK).hardnessAndResistance(3.0f))
object AntiAirBlock : AirBlock(Properties.create(Material.AIR).doesNotBlockMovement().noDrops().notSolid()), INonItem
class FluidBlock(fluid : () -> FlowingFluid) : FlowingFluidBlock(fluid, Properties.create(Material.WATER).doesNotBlockMovement().lightValue(15).hardnessAndResistance(100.0f).noDrops()), INonTab
