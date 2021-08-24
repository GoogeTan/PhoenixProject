package phoenix.init

import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix
import phoenix.other.build
import phoenix.tile.ash.HandMillTile
import phoenix.tile.ash.SolarDryerTile
import phoenix.tile.ash.OvenTile
import phoenix.tile.ash.PotteryBarrelTile
import phoenix.tile.redo.CeramicTile
import phoenix.tile.redo.ElectricBarrelTile
import phoenix.tile.redo.JuicerTile
import phoenix.tile.redo.TankTile
import phoenix.tile.redo.pipe.BambooPipeTile
import phoenix.tile.redo.pipe.TurnBambooPipeTile
import phoenix.tile.redo.pipe.VerticalBambooPipeTile
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhxTiles
{
    val TILE_ENTITIES = KDeferredRegister(ForgeRegistries.TILE_ENTITIES, Phoenix.MOD_ID)

    val tank            : TileEntityType<TankTile>           by TILE_ENTITIES.register("tank")           { TileEntityType.Builder.create(::TankTile, PhxBlocks.tank).build() }
    val bambooPipe          : TileEntityType<BambooPipeTile> by TILE_ENTITIES.register("bamboo_pipe")    { TileEntityType.Builder.create(::BambooPipeTile, PhxBlocks.bambooPipe).build() }
    val verticalBambooPipe  : TileEntityType<VerticalBambooPipeTile> by TILE_ENTITIES.register("vertical_bamboo_pipe") { TileEntityType.Builder.create(::VerticalBambooPipeTile, PhxBlocks.bambooPipe).build() }
    val turnBambooPipe      : TileEntityType<TurnBambooPipeTile>     by TILE_ENTITIES.register("turn_bamboo_pipe")     { TileEntityType.Builder.create(::TurnBambooPipeTile, PhxBlocks.bambooPipe).build() }

    val potteryBarrel  : TileEntityType<PotteryBarrelTile>  by TILE_ENTITIES.register("pottery_barrel") { TileEntityType.Builder.create(::PotteryBarrelTile, PhxBlocks.potteryBarrel).build() }
    val oven           : TileEntityType<OvenTile>           by TILE_ENTITIES.register("oven")           { TileEntityType.Builder.create(::OvenTile, PhxBlocks.oven).build() }
    val electricBarrel : TileEntityType<ElectricBarrelTile> by TILE_ENTITIES.register("electric_barrel"){ TileEntityType.Builder.create(::ElectricBarrelTile, PhxBlocks.potteryBarrel).build() }
    val juicer         : TileEntityType<JuicerTile>         by TILE_ENTITIES.register("juicer")         { TileEntityType.Builder.create(::JuicerTile, PhxBlocks.juicer).build() }
    val ceramic        : TileEntityType<CeramicTile>        by TILE_ENTITIES.register("ceramic")        { TileEntityType.Builder.create(::CeramicTile, PhxBlocks.ceramic).build() }
    val solarDryer     : TileEntityType<SolarDryerTile>     by TILE_ENTITIES.register("solar_dryer")     { TileEntityType.Builder.create(::SolarDryerTile, PhxBlocks.solarDryer).build() }
    val handMill       : TileEntityType<HandMillTile>       by TILE_ENTITIES.register("hand_mill")      { TileEntityType.Builder.create(::HandMillTile,   PhxBlocks.handMill).build() }

    fun register() = TILE_ENTITIES.register(MOD_BUS)
}