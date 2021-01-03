package phoenix.init

import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix
import phoenix.tile.ash.OvenTile
import phoenix.tile.ash.PotteryBarrelTile
import phoenix.tile.redo.ElectricBarrelTile
import phoenix.tile.redo.PipeTile
import phoenix.tile.redo.TankTile

object PhoenixTiles
{
    @JvmStatic val TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Phoenix.MOD_ID)!!

    @JvmStatic val TANK           = TILE_ENTITIES.register("tank")           { TileEntityType.Builder.create({ TankTile() }, PhoenixBlocks.TANK.get()).build(null) }!!
    @JvmStatic val PIPE           = TILE_ENTITIES.register("pipe")           { TileEntityType.Builder.create({ PipeTile() }, PhoenixBlocks.PIPE.get()).build(null) }!!
    @JvmStatic val POTTERY_BARREL = TILE_ENTITIES.register("pottery_barrel") { TileEntityType.Builder.create({ PotteryBarrelTile() }, PhoenixBlocks.POTTERY_BARREL.get()).build(null) }!!
    @JvmStatic val OVEN           = TILE_ENTITIES.register("oven")           { TileEntityType.Builder.create({ OvenTile() }, PhoenixBlocks.OVEN.get()).build(null) }!!
    @JvmStatic val ELECTRIC_BARREL = TILE_ENTITIES.register("_barrel") { TileEntityType.Builder.create({ ElectricBarrelTile() }, PhoenixBlocks.POTTERY_BARREL.get()).build(null) }!!


    //@JvmStatic val TEXT           = TILE_ENTITIES.register("text")           { TileEntityType.Builder.create({ TextTile() }, PhoenixBlocks.TEXT_BLOCK.get()).build(null) }!!

    @JvmStatic fun register() = TILE_ENTITIES.register(FMLJavaModLoadingContext.get().modEventBus)
}