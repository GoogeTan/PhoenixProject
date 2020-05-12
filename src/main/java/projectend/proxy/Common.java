package projectend.proxy;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import projectend.BlocksRegister;
import projectend.blocks.Updater;
import projectend.blocks.title.Updator01TitleEntity;
import projectend.blocks.unit03.BlockMindOre;
import projectend.world.BiomeRegistrar;
import projectend.world.WorldProviderEndBiomes;
import projectend.world.capablity.IStager;
import projectend.world.capablity.StageHandler;
import projectend.world.capablity.StageStorage;
import projectend.world.structures.Unit01.WorldGenCorn;

public class Common
{

    public void preInit(FMLPreInitializationEvent event)
    {
        BlocksRegister.register();
        CapabilityManager.INSTANCE.register(IStager.class, new StageStorage(), StageHandler.class);//reg capablity
        GameRegistry.registerTileEntity(Updator01TitleEntity.class, new Updater().getRegistryName().toString());//hand reg tile entity
    }
    public void init(FMLInitializationEvent event)
    {
        overrideEnd();
        BiomeRegistrar.registerBiomes();
        GameRegistry.registerWorldGenerator(new WorldGenCorn(), 5);
    }
    public void postInit(FMLPostInitializationEvent event) {  }

    public void overrideEnd()
    {
        DimensionManager.unregisterDimension(1);
        DimensionType endBiomes = DimensionType.register("End", "_end", 1, WorldProviderEndBiomes.class, false);
        DimensionManager.registerDimension(1, endBiomes);
    }
}