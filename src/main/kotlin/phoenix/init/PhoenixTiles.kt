package phoenix.init

import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix
import phoenix.tile.TextTile
import phoenix.tile.ash.OvenTile
import phoenix.tile.ash.PotteryBarrelTile
import phoenix.tile.redo.ElectricBarrelTile
import phoenix.tile.redo.PipeTile
import phoenix.tile.redo.TankTile
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhoenixTiles
{
    val TILE_ENTITIES = KDeferredRegister(ForgeRegistries.TILE_ENTITIES, Phoenix.MOD_ID)

    val TANK            by TILE_ENTITIES.register("tank")           { TileEntityType.Builder.create(::TankTile, PhoenixBlocks.TANK).build(null) }
    val PIPE            by TILE_ENTITIES.register("pipe")           { TileEntityType.Builder.create(::PipeTile, PhoenixBlocks.PIPE).build(null) }
    val POTTERY_BARREL  by TILE_ENTITIES.register("pottery_barrel") { TileEntityType.Builder.create(::PotteryBarrelTile, PhoenixBlocks.POTTERY_BARREL).build(null) }
    val OVEN            by TILE_ENTITIES.register("oven")           { TileEntityType.Builder.create(::OvenTile, PhoenixBlocks.OVEN).build(null) }
    val ELECTRIC_BARREL by TILE_ENTITIES.register("_barrel")        { TileEntityType.Builder.create(::ElectricBarrelTile, PhoenixBlocks.POTTERY_BARREL).build(null) }
    val TEXT            by TILE_ENTITIES.register("text")           { TileEntityType.Builder.create(::TextTile, PhoenixBlocks.TEXT_BLOCK).build(null) }

    fun register() = TILE_ENTITIES.register(MOD_BUS)
}