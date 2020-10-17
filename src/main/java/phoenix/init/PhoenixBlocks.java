package phoenix.init;

import net.minecraft.block.Block;
import net.minecraft.block.ChorusPlantBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import phoenix.Phoenix;
import phoenix.blocks.AntiAirBlock;
import phoenix.blocks.UpdaterBlock;
import phoenix.blocks.ash.EndStoneColumnBlock;
import phoenix.blocks.ash.OvenBlock;
import phoenix.blocks.ash.PotteryBarrelBlock;
import phoenix.blocks.redo.*;
import phoenix.utils.INonItem;

@Mod.EventBusSubscriber(modid = Phoenix.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PhoenixBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Phoenix.MOD_ID);

    public static final RegistryObject<Block>            UPDATOR           = BLOCKS.register("updater",           UpdaterBlock        ::new);
    public static final RegistryObject<Block>            PIPE              = BLOCKS.register("pipe",              PipeBlock           ::new);
    public static final RegistryObject<Block>            TANK              = BLOCKS.register("tank",              TankBlock           ::new);
    public static final RegistryObject<Block>            FERTILE_END_STONE = BLOCKS.register("fertile_end_stone", FertileEndStoneBlock::new);
    public static final RegistryObject<ChorusPlantBlock> KIKIN_STEAM       = BLOCKS.register("kikin_stem",        KikinStemBlock      ::new);
    public static final RegistryObject<Block>            KIKIN_FRUIT       = BLOCKS.register("kikin_fruit",       KikiNFruitBlock     ::new);
    public static final RegistryObject<Block>            ANTI_AIR          = BLOCKS.register("anti_air",          AntiAirBlock        ::new);
    public static final RegistryObject<Block>            POTTERY_BARREL    = BLOCKS.register("pottery_barrel",    PotteryBarrelBlock  ::new);
    public static final RegistryObject<Block>            END_STONE_COLUMN  = BLOCKS.register("end_stone_column",  EndStoneColumnBlock ::new);
    public static final RegistryObject<Block>            OVEN              = BLOCKS.register("oven",              OvenBlock           ::new);


    public static void register()
    {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event)
    {
        final IForgeRegistry<Item> registry = event.getRegistry();
        PhoenixBlocks.BLOCKS.getEntries().stream()
                .map(RegistryObject::get)
                .filter(block -> !(block instanceof INonItem))
                .filter(block -> !(block instanceof FlowingFluidBlock))
                .forEach(block ->
                {
                    final Item.Properties prop = new Item.Properties().group(Phoenix.PHOENIX);
                    final BlockItem blockItem = new BlockItem(block, prop);
                    blockItem.setRegistryName(block.getRegistryName());
                    registry.register(blockItem);
                });
    }
}