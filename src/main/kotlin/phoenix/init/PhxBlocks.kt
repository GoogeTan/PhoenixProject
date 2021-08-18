package phoenix.init

import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.block.material.MaterialColor
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.world.storage.loot.LootContext
import net.minecraftforge.common.ToolType
import net.minecraftforge.registries.ForgeRegistries
import phoenix.MOD_ID
import phoenix.Phoenix
import phoenix.api.block.FluidBlock
import phoenix.api.block.ICustomGroup
import phoenix.api.block.INonItem
import phoenix.api.block.IRedoThink
import phoenix.blocks.UpdaterBlock
import phoenix.blocks.ash.OvenBlock
import phoenix.blocks.ash.PotteryBarrelBlock
import phoenix.blocks.redo.*
import phoenix.blocks.redo.pipe.BambooPipeBlock
import phoenix.blocks.redo.pipe.TurnBambooPipeBlock
import phoenix.blocks.redo.pipe.VerticalBambooPipeBlock
import phoenix.other.register
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhxBlocks
{
    val blocks = KDeferredRegister(ForgeRegistries.BLOCKS, MOD_ID)

    val updater            by blocks.register("updater", UpdaterBlock)
    val bambooPipe         by blocks.register("bamboo_pipe", BambooPipeBlock)
    val verticalBambooPipe by blocks.register("vertical_bamboo_pipe", VerticalBambooPipeBlock)
    val turnBambooPipe     by blocks.register("turn_bamboo_pipe", TurnBambooPipeBlock)
    val tank               by blocks.register("tank", TankBlock)
    val juicer             by blocks.register("juicer", JuicerBlock)

    val fertileEndStone    by blocks.register("fertile_end_stone", FertileEndStoneBlock)
    val antiAir: AirBlock  by blocks.register("anti_air") { object : AirBlock(Properties.create(Material.AIR).doesNotBlockMovement().noDrops().notSolid()), INonItem {} }
    val potteryBarrel      by blocks.register("pottery_barrel", ::PotteryBarrelBlock)
    val electricBarrel     by blocks.register("electric_barrel", ::ElectricBarrelBlock)
    val endStoneColumn     by blocks.register("end_stone_column", RotatedPillarBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0f)))
    val chiseledEndStone   by blocks.register("chiseled_end_stone", RotatedPillarBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0f)))

    val oven               by blocks.register("oven", ::OvenBlock)
    val seta               by blocks.register("seta", SetaBlock)
    val zirconium          by blocks.register("zirconium_ore")
    {
        object : OreBlock(Properties.create(Material.ROCK).hardnessAndResistance(3f).harvestTool(ToolType.PICKAXE))
        {
            override fun getDrops(state: BlockState, builder: LootContext.Builder) = listOf(ItemStack(this))
        }
    }

    val armoredGlass       by blocks.register("armored_glass", ArmoredGlassBlock)
    val wetLog             by blocks.register("wet_log", WetLogBlock)
    val diedWetLog      : Block by blocks.register("died_wet_log")    { object : RotatedPillarBlock(Properties.create(Material.WOOD).hardnessAndResistance(3.0f)), ICustomGroup { override val tab: ItemGroup = Phoenix.REDO } }
    val wetPlanks       : Block by blocks.register("wet_planks")      { object : LogBlock(MaterialColor.SAND, Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(2.0f, 3.0f).sound(SoundType.WOOD)), ICustomGroup { override val tab: ItemGroup = Phoenix.REDO } }
    val diedWetPlanks   : Block by blocks.register("died_wet_planks") { object : LogBlock(MaterialColor.SAND, Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(2.0f, 3.0f).sound(SoundType.WOOD)), ICustomGroup { override val tab: ItemGroup = Phoenix.REDO } }
    val wetSlab         : Block by blocks.register("wet_slab")        { object : SlabBlock(Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(2.0f, 3.0f).sound(SoundType.WOOD)), ICustomGroup { override val tab: ItemGroup = Phoenix.REDO } }
    val diedWetSlab     : Block by blocks.register("died_wet_slab")   { object : SlabBlock(Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(2.0f, 3.0f).sound(SoundType.WOOD)), ICustomGroup { override val tab: ItemGroup = Phoenix.REDO } }
    val wetStairs       : Block by blocks.register("wet_stairs")      { object : StairsBlock(wetLog::getDefaultState,     Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(2.0f, 3.0f).sound(SoundType.WOOD)), ICustomGroup { override val tab: ItemGroup = Phoenix.REDO } }
    val diedWetStairs   : Block by blocks.register("died_wet_stairs") { object : StairsBlock(diedWetLog::getDefaultState, Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(2.0f, 3.0f).sound(SoundType.WOOD)), ICustomGroup { override val tab: ItemGroup = Phoenix.REDO } }
    val setaJuice       : FluidBlock by blocks.register("seta_juice", FluidBlock(PhxFluids::seta_juice_source))
    val ceramic         : Block by blocks.register("ceramic_bricks")  { object : Block(Properties.create(Material.ROCK).sound(SoundType.STONE)), IRedoThink { }  }

    fun register() = blocks.register(MOD_BUS)
}

