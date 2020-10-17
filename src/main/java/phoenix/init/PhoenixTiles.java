package phoenix.init;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import phoenix.Phoenix;
import phoenix.tile.PipeTile;
import phoenix.tile.PotteryBarrelTile;
import phoenix.tile.TankTile;
import phoenix.tile.UpdaterTile;
import phoenix.tile.ash.OvenTile;

public class PhoenixTiles
{
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Phoenix.MOD_ID);

    public static final RegistryObject<TileEntityType<UpdaterTile>>      UPDATOR        = TILE_ENTITIES.register("updator",
            () -> TileEntityType.Builder.create(UpdaterTile::new,  PhoenixBlocks.UPDATOR.get()).build(null));
    public static final RegistryObject<TileEntityType<TankTile>>          TANK           = TILE_ENTITIES.register("tank",
            () -> TileEntityType.Builder.create(TankTile::new, PhoenixBlocks.TANK.get()).build(null));
    public static final RegistryObject<TileEntityType<PipeTile>>          PIPE           = TILE_ENTITIES.register("pipe",
            () -> TileEntityType.Builder.create(PipeTile::new, PhoenixBlocks.PIPE.get()).build(null));
    public static final RegistryObject<TileEntityType<PotteryBarrelTile>> POTTERY_BARREL = TILE_ENTITIES.register("pottery_barrel",
            () -> TileEntityType.Builder.create(PotteryBarrelTile::new, PhoenixBlocks.POTTERY_BARREL.get()).build(null));
    public static final RegistryObject<TileEntityType<OvenTile>> OVEN           = TILE_ENTITIES.register("oven",
            () -> TileEntityType.Builder.create(OvenTile::new,  PhoenixBlocks.OVEN.get()).build(null));

    public static void register()
    {
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}