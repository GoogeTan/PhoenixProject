package phoenix.init

import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix
import phoenix.tile.ash.OvenTile
import phoenix.tile.ash.PotteryBarrelTile
import phoenix.tile.redo.ElectricBarrelTile
import phoenix.tile.redo.JuicerTile
import phoenix.tile.redo.PipeTile
import phoenix.tile.redo.TankTile
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import phoenix.utils.build

object PhxTiles
{
    val TILE_ENTITIES = KDeferredRegister(ForgeRegistries.TILE_ENTITIES, Phoenix.MOD_ID)

    val TANK            by TILE_ENTITIES.register("tank")           { TileEntityType.Builder.create(::TankTile, PhxBlocks.TANK).build() }
    val PIPE            by TILE_ENTITIES.register("pipe")           { TileEntityType.Builder.create(::PipeTile, PhxBlocks.PIPE).build() }
    val POTTERY_BARREL  by TILE_ENTITIES.register("pottery_barrel") { TileEntityType.Builder.create(::PotteryBarrelTile, PhxBlocks.POTTERY_BARREL).build() }
    val OVEN            by TILE_ENTITIES.register("oven")           { TileEntityType.Builder.create(::OvenTile, PhxBlocks.OVEN).build() }
    val ELECTRIC_BARREL by TILE_ENTITIES.register("electric_barrel"){ TileEntityType.Builder.create(::ElectricBarrelTile, PhxBlocks.POTTERY_BARREL).build() }
    val JUICER          by TILE_ENTITIES.register("juicer"){ TileEntityType.Builder.create(::JuicerTile, PhxBlocks.SETA_JUICE).build() }

    //val TEXT            by TILE_ENTITIES.register("text")           { TileEntityType.Builder.create(::TextTile, PhxBlocks.TEXT_BLOCK).build(null) }

    fun register() = TILE_ENTITIES.register(MOD_BUS)
}