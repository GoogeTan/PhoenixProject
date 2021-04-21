package phoenix.init

import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.block.material.MaterialColor
import net.minecraft.fluid.FlowingFluid
import net.minecraft.item.ItemGroup
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
import phoenix.utils.block.ICustomGroup
import phoenix.utils.block.INonItem
import phoenix.utils.block.INonTab
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhxBlocks
{
    val BLOCKS = KDeferredRegister(ForgeRegistries.BLOCKS, Phoenix.MOD_ID)

    val UPDATER           by  BLOCKS.register("updater") { UpdaterBlock         }
    val PIPE              by  BLOCKS.register("pipe", ::PipeBlock)
    val TANK              by  BLOCKS.register("tank") { TankBlock            }
    val FERTILE_END_STONE by  BLOCKS.register("fertile_end_stone") { FertileEndStoneBlock }
    val ANTI_AIR: AirBlock by BLOCKS.register("anti_air") { object : AirBlock(Properties.create(Material.AIR).doesNotBlockMovement().noDrops().notSolid()), INonItem {} }
    val POTTERY_BARREL    by  BLOCKS.register("pottery_barrel", ::PotteryBarrelBlock)
    val ELECTRIC_BARREL   by  BLOCKS.register("electric_barrel", ::ElectricBarrelBlock)
    val END_STONE_COLUMN  by  BLOCKS.register("end_stone_column") { RotatedPillarBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0f))  }
    val OVEN              by  BLOCKS.register("oven", ::OvenBlock)
    val SETA              by  BLOCKS.register("seta") { SetaBlock            }
    val ZIRCONIUM         by  BLOCKS.register("zirconium_ore") { ZirconiumOreBlock    }
    val TEXT_BLOCK        by  BLOCKS.register("block_with_text") { AnonimBlock.create(Material.ROCK, ::TextTile, Phoenix.REDO) }
    val ARMORED_GLASS     by  BLOCKS.register("armored_glass") { ArmoredGlassBlock    }
    val WET_LOG           by  BLOCKS.register("wet_log") { WetLogBlock   }
    val DIED_WET_LOG: RotatedPillarBlock by BLOCKS.register("died_wet_log") { object : RotatedPillarBlock(Properties.create(Material.WOOD).hardnessAndResistance(3.0f)), ICustomGroup { override val tab: ItemGroup = Phoenix.REDO } }
    val WET_PLANKS: Block by  BLOCKS.register("wet_planks") { object : Block(Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(2.0f, 3.0f).sound(SoundType.WOOD)), ICustomGroup { override val tab: ItemGroup = Phoenix.REDO } }
    val SETA_JUICE        by BLOCKS.register("seta_juice") { FluidBlock(PhxFluids::seta_juice_source) }

    fun register() = BLOCKS.register(MOD_BUS)
}

object ZirconiumOreBlock : OreBlock(Properties.create(Material.ROCK).hardnessAndResistance(3f).harvestTool(ToolType.PICKAXE))
{
    override fun getDrops(state: BlockState, builder: LootContext.Builder) = listOf(ItemStack(this))
}

class FluidBlock(fluidSource : () -> FlowingFluid) : FlowingFluidBlock(fluidSource, Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0f).noDrops().notSolid()), INonTab
