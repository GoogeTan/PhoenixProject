package projectend.init;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import projectend.ProjectEnd;
import projectend.blocks.GostBlock;
import projectend.blocks.HelpBlock;
import projectend.blocks.TestBlock;
import projectend.blocks.Updater;
import projectend.blocks.title.GostBlockTile;
import projectend.blocks.title.Updator01TitleEntity;
import projectend.blocks.unit02.BlockGoodEndStone;
import projectend.blocks.unit03.BlockMindOre;
import projectend.blocks.unit03.BlockSteelBone;

@Mod.EventBusSubscriber(modid = ProjectEnd.MOD_ID)
public class BlocksRegister
{
    public static Block MIND = new BlockMindOre();
    public static Block GOST = new GostBlock();
    public static Block HELP = new HelpBlock();
    public static Block TEST = new TestBlock();
    public static Block UPDATER = new Updater();
    public static Block FERTILE_END_STONE = new BlockGoodEndStone();
    public static Block CERAOMC = new BlockSteelBone();
    public static ModelResourceLocation gost_model;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().registerAll
        (
                MIND,
                GOST,
                HELP,
                TEST,
                UPDATER,
                FERTILE_END_STONE,
                CERAOMC
        );
        GameRegistry.registerTileEntity(Updator01TitleEntity.class, UPDATER.getRegistryName().toString());//hand reg tile entity
        GameRegistry.registerTileEntity(GostBlockTile.class,           GOST.getRegistryName().toString());//hand reg tile entity
        //gost_model = new ModelResourceLocation(event.getRegistry().getKey(GOST), "normal");

        /*ModelLoader.setCustomStateMapper(GOST, new StateMapperBase()
        {
            protected ModelResourceLocation getModelResourceLocation(IBlockState p_178132_1_){  return gost_model;  }
        });*/
    }
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll
        (
               new ItemBlock(MIND).setRegistryName(MIND.getRegistryName()),
               new ItemBlock(GOST).setRegistryName(GOST.getRegistryName()),
               new ItemBlock(HELP).setRegistryName(HELP.getRegistryName()),
               new ItemBlock(TEST).setRegistryName(TEST.getRegistryName()),
               new ItemBlock(UPDATER).setRegistryName(UPDATER.getRegistryName()),
               new ItemBlock(FERTILE_END_STONE).setRegistryName(FERTILE_END_STONE.getRegistryName()),
               new ItemBlock(CERAOMC).setRegistryName(CERAOMC.getRegistryName())
        );
    }



    /*@SubscribeEvent model todo
    public static void onModelBakeEvent(ModelBakeEvent event)
    {
       TextureAtlasSprite crystalTexture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("thaumcraft:blocks/crystal");
       IBakedModel customCrystalModel = new ModelMindOre(crystalTexture);
       event.getModelRegistry().putObject(gost_model, customCrystalModel);
    }*/

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
