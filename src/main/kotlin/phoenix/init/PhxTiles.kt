package phoenix.init

import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix
import phoenix.tile.ash.OvenTile
import phoenix.tile.ash.PotteryBarrelTile
import phoenix.tile.redo.ElectricBarrelTile
import phoenix.tile.redo.JuicerTile
import phoenix.tile.redo.BambooPipeTile
import phoenix.tile.redo.TankTile
import phoenix.utils.build
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhxTiles
{
    val TILE_ENTITIES = KDeferredRegister(ForgeRegistries.TILE_ENTITIES, Phoenix.MOD_ID)

    val tank            : TileEntityType<TankTile>           by TILE_ENTITIES.register("tank")           { TileEntityType.Builder.create(::TankTile, PhxBlocks.tank).build() }
    val bambooPipe      : TileEntityType<BambooPipeTile>     by TILE_ENTITIES.register("bamboo_pipe")    { TileEntityType.Builder.create(::BambooPipeTile, PhxBlocks.bambooPipe).build() }
    val POTTERY_BARREL  : TileEntityType<PotteryBarrelTile>  by TILE_ENTITIES.register("pottery_barrel") { TileEntityType.Builder.create(::PotteryBarrelTile, PhxBlocks.potteryBarrel).build() }
    val OVEN            : TileEntityType<OvenTile>           by TILE_ENTITIES.register("oven")           { TileEntityType.Builder.create(::OvenTile, PhxBlocks.oven).build() }
    val ELECTRIC_BARREL : TileEntityType<ElectricBarrelTile> by TILE_ENTITIES.register("electric_barrel"){ TileEntityType.Builder.create(::ElectricBarrelTile, PhxBlocks.potteryBarrel).build() }
    val JUICER          : TileEntityType<JuicerTile>         by TILE_ENTITIES.register("juicer")         { TileEntityType.Builder.create(::JuicerTile, PhxBlocks.juicer).build() }

    //val TEXT            by TILE_ENTITIES.register("text")           { TileEntityType.Builder.create(::TextTile, PhxBlocks.textBlock).build(null) }

    fun register() = TILE_ENTITIES.register(MOD_BUS)
}