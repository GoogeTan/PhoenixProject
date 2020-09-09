package phoenix.init;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import phoenix.Phoenix;
import phoenix.tile.PipeTile;
import phoenix.tile.TankTile;
import phoenix.tile.UpdaterTile;

public class PhoenixTiles
{
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Phoenix.MOD_ID);

    public static final RegistryObject<TileEntityType<UpdaterTile>> UPDATOR = TILE_ENTITIES.register("updator", () -> TileEntityType.Builder.create(UpdaterTile::new,
            PhoenixBlocks.UPDATOR.get()).build(null));
    public static final RegistryObject<TileEntityType<TankTile>>    TANK    = TILE_ENTITIES.register("tank",    () -> TileEntityType.Builder.create(TankTile::new,
            PhoenixBlocks.TANK.get()).build(null));
    public static final RegistryObject<TileEntityType<PipeTile>>    PIPE    = TILE_ENTITIES.register("pipe",    () -> TileEntityType.Builder.create(PipeTile::new,
            PhoenixBlocks.PIPE.get()).build(null));

    public static void register()
    {
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}