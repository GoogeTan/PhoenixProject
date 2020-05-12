package projectend;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import projectend.blocks.GostBlock;
import projectend.blocks.HelpBlock;
import projectend.blocks.TestBlock;
import projectend.blocks.Updater;
import projectend.blocks.title.GostBlockTile;
import projectend.blocks.title.Updator01TitleEntity;
import projectend.blocks.unit02.BlockGoodEndStone;
import projectend.blocks.unit03.BlockMindOre;

public class BlocksRegister
{
    public static Block MIND = new BlockMindOre();
    public static Block GOST = new GostBlock();
    public static Block HELP = new HelpBlock();
    public static Block TEST = new TestBlock();
    public static Block UPDATER = new Updater();
    public static Block FERTILE_END_STONE = new BlockGoodEndStone();

    public static void register()
    {
        setRegister(MIND);
        setRegister(GOST);
        setRegister(HELP);
        setRegister(TEST);
        setRegister(UPDATER);
        setRegister(FERTILE_END_STONE);
        GameRegistry.registerTileEntity(Updator01TitleEntity.class, UPDATER.getRegistryName().toString());//hand reg tile entity
        GameRegistry.registerTileEntity(GostBlockTile.class,           GOST.getRegistryName().toString());//hand reg tile entity
    }

    @SideOnly(Side.CLIENT)
    public static void registerRender()
    {
        setRender(MIND);
        setRender(GOST);
        setRender(HELP);
        setRender(TEST);
        setRender(UPDATER);
        setRender(FERTILE_END_STONE);
    }

    private static void setRegister(Block block)
    {
        ForgeRegistries.BLOCKS.register(block);
        ForgeRegistries.ITEMS.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }

    @SideOnly(Side.CLIENT)
    private static void setRender(Block block)
    {

    }
}
