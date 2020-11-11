package phoenix.init;

import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import phoenix.Phoenix;
import phoenix.blocks.AntiAirBlock;
import phoenix.blocks.UpdaterBlock;
import phoenix.blocks.ash.EndStoneColumnBlock;
import phoenix.blocks.ash.OvenBlock;
import phoenix.blocks.ash.PotteryBarrelBlock;
import phoenix.blocks.ash.ZirconiumOreBlock;
import phoenix.blocks.redo.*;

@Mod.EventBusSubscriber(modid = Phoenix.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PhoenixBlocks
{
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister(ForgeRegistries.BLOCKS, Phoenix.MOD_ID);

    public static final RegistryObject<Block>   UPDATER           = BLOCKS.register("updater",           UpdaterBlock        ::new);
    public static final RegistryObject<Block>   PIPE              = BLOCKS.register("pipe",              PipeBlock           ::new);
    public static final RegistryObject<Block>   TANK              = BLOCKS.register("tank",              TankBlock           ::new);
    public static final RegistryObject<Block>   FERTILE_END_STONE = BLOCKS.register("fertile_end_stone", FertileEndStoneBlock::new);
    public static final RegistryObject<Block>   KIKIN_STEAM       = BLOCKS.register("kikin_stem",        KikinStemBlock      ::new);
    public static final RegistryObject<Block>   KIKIN_FRUIT       = BLOCKS.register("kikin_fruit",       KikiNFruitBlock     ::new);
    public static final RegistryObject<Block>   ANTI_AIR          = BLOCKS.register("anti_air",          AntiAirBlock        ::new);
    public static final RegistryObject<Block>   POTTERY_BARREL    = BLOCKS.register("pottery_barrel",    PotteryBarrelBlock  ::new);
    public static final RegistryObject<Block>   END_STONE_COLUMN  = BLOCKS.register("end_stone_column",  EndStoneColumnBlock ::new);
    public static final RegistryObject<Block>   OVEN              = BLOCKS.register("oven",              OvenBlock           ::new);
    public static final RegistryObject<Block>   ZIRCONIUM         = BLOCKS.register("zirconium_ore",     ZirconiumOreBlock   ::new);

    public static void register()
    {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}