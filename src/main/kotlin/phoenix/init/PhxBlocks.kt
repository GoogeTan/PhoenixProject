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

    val updater           by  BLOCKS.register("updater") { UpdaterBlock         }
    val pipe              by  BLOCKS.register("pipe", ::PipeBlock)
    val tank              by  BLOCKS.register("tank") { TankBlock            }
    val fertileEndStone   by  BLOCKS.register("fertile_end_stone") { FertileEndStoneBlock }
    val antiAir: AirBlock by BLOCKS.register("anti_air") { object : AirBlock(Properties.create(Material.AIR).doesNotBlockMovement().noDrops().notSolid()), INonItem {} }
    val potteryBarrel     by  BLOCKS.register("pottery_barrel", ::PotteryBarrelBlock)
    val electricBarrel    by  BLOCKS.register("electric_barrel", ::ElectricBarrelBlock)
    val endStoneColumn    by  BLOCKS.register("end_stone_column") { RotatedPillarBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0f))  }
    val oven              by  BLOCKS.register("oven", ::OvenBlock)
    val seta              by  BLOCKS.register("seta") { SetaBlock            }
    val zirconium         by  BLOCKS.register("zirconium_ore") { ZirconiumOreBlock    }
    val textBlock         by  BLOCKS.register("block_with_text") { AnonimBlock.create(Material.ROCK, ::TextTile, Phoenix.REDO) }
    val armoredGlass      by  BLOCKS.register("armored_glass") { ArmoredGlassBlock    }
    val wetLog            by  BLOCKS.register("wet_log") { WetLogBlock   }
    val diedWetLog      : Block by BLOCKS.register("died_wet_log") { object : RotatedPillarBlock(Properties.create(Material.WOOD).hardnessAndResistance(3.0f)), ICustomGroup { override val tab: ItemGroup = Phoenix.REDO } }
    val wetPlanks       : Block by BLOCKS.register("wet_planks")      { object : LogBlock(MaterialColor.SAND, Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(2.0f, 3.0f).sound(SoundType.WOOD)), ICustomGroup { override val tab: ItemGroup = Phoenix.REDO } }
    val diedWetPlanks   : Block by BLOCKS.register("died_wet_planks") { object : LogBlock(MaterialColor.SAND, Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(2.0f, 3.0f).sound(SoundType.WOOD)), ICustomGroup { override val tab: ItemGroup = Phoenix.REDO } }
    val wetSlab         : Block by BLOCKS.register("wet_slab")        { object : SlabBlock(Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(2.0f, 3.0f).sound(SoundType.WOOD)), ICustomGroup { override val tab: ItemGroup = Phoenix.REDO } }
    val diedWetSlab     : Block by BLOCKS.register("died_wet_slab")   { object : SlabBlock(Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(2.0f, 3.0f).sound(SoundType.WOOD)), ICustomGroup { override val tab: ItemGroup = Phoenix.REDO } }
    val wetStairs       : Block by BLOCKS.register("wet_stairs")      { object : StairsBlock(wetLog::getDefaultState,     Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(2.0f, 3.0f).sound(SoundType.WOOD)), ICustomGroup { override val tab: ItemGroup = Phoenix.REDO } }
    val diedWetStairs   : Block by BLOCKS.register("died_wet_stairs") { object : StairsBlock(diedWetLog::getDefaultState, Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(2.0f, 3.0f).sound(SoundType.WOOD)), ICustomGroup { override val tab: ItemGroup = Phoenix.REDO } }
    val setaJuice             by BLOCKS.register("seta_juice") { FluidBlock(PhxFluids::seta_juice_source) }

    fun register() = BLOCKS.register(MOD_BUS)
}

object ZirconiumOreBlock : OreBlock(Properties.create(Material.ROCK).hardnessAndResistance(3f).harvestTool(ToolType.PICKAXE))
{
    override fun getDrops(state: BlockState, builder: LootContext.Builder) = listOf(ItemStack(this))
}

class FluidBlock(fluidSource : () -> FlowingFluid) : FlowingFluidBlock(fluidSource, Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0f).noDrops().notSolid()), INonTab
